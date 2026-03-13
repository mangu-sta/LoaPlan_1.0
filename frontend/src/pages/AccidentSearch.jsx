import { useState, useEffect, useRef } from "react";
import InvenResultModal from "../components/InvenResultModal";

export default function AccidentSearch() {
  const [keyword, setKeyword] = useState("");
  const [results, setResults] = useState([]);
  const [openModal, setOpenModal] = useState(false);

  const [page, setPage] = useState(1);
  const [loading, setLoading] = useState(false);
  const [hasMore, setHasMore] = useState(true);

  const loaderRef = useRef(null);

  // 🔥 특정 페이지를 불러오는 함수
  const fetchPage = async (pageNum) => {
    setLoading(true);
    try {
      const res = await fetch(
        `/api/inven/search?q=${encodeURIComponent(keyword)}&page=${pageNum}`
      );
      if (!res.ok) {
        console.error("API 요청 실패:", res.status);
        setLoading(false);
        return [];
      }
      const data = await res.json();

      if (data.length === 0) {
        setHasMore(false); // 더 이상 페이지 없음
      }

      setLoading(false);
      return data;
    } catch (e) {
      console.error("요청 오류:", e);
      setLoading(false);
      setHasMore(false);
      return [];
    }
  };

  // 🔎 검색 버튼 클릭 / 엔터
  const handleSearch = async (e) => {
    e.preventDefault();
    if (!keyword.trim()) return;

    // 새 검색이므로 상태 리셋
    setResults([]);
    setPage(1);
    setHasMore(true);
    setOpenModal(true);

    const firstResults = await fetchPage(1);
    setResults(firstResults);
  };

  // 🔥 무한스크롤: loaderRef가 화면에 보이면 다음 페이지 로드
  useEffect(() => {
    if (!openModal || !hasMore) return;

    const observer = new IntersectionObserver(
      async (entries) => {
        const target = entries[0];
        if (target.isIntersecting && !loading) {
          const nextPage = page + 1;
          const more = await fetchPage(nextPage);

          if (more.length > 0) {
            setResults((prev) => [...prev, ...more]);
            setPage(nextPage);
          }
        }
      },
      { threshold: 1 }
    );

    if (loaderRef.current) {
      observer.observe(loaderRef.current);
    }

    return () => {
      if (loaderRef.current) {
        observer.unobserve(loaderRef.current);
      }
    };
  }, [openModal, page, loading, hasMore, keyword]);

  return (
    <div className="page-container">
      <h2>📰 사사게 검색</h2>

      <form onSubmit={handleSearch}>
        <input
          type="text"
          placeholder="검색어 입력"
          value={keyword}
          onChange={(e) => setKeyword(e.target.value)}
        />
        <button type="submit">검색</button>
      </form>

      {openModal && (
        <InvenResultModal
          results={results}
          loading={loading}
          loaderRef={loaderRef}
          keyword={keyword}
          onClose={() => setOpenModal(false)}
        />
      )}
    </div>
  );
}
