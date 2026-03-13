import { useState } from "react";
import CharacterSearchModal from "../components/CharacterSearchModal";

export default function CharacterSearch() {
  const [name, setName] = useState("");
  const [data, setData] = useState(null);
  const [open, setOpen] = useState(false);

  // 🛠️ 검색 로직 분리
  const performSearch = async (targetName) => {
    if (!targetName || !targetName.trim()) return;

    try {
      // 1) 캐릭터 정보 조회
      const res = await fetch(`/api/armory/search?nickname=${targetName}`);
      if (!res.ok) {
        alert("캐릭터를 찾을 수 없습니다.");
        return;
      }

      const json = await res.json();

      // 2) 사사게 검색 (닉네임 그대로 검색)
      const invenRes = await fetch(`/api/inven/search?q=${targetName}&page=1`);
      const invenJson = invenRes.ok ? await invenRes.json() : [];

      // 🔥 이용규칙 글 제외
      const realPosts = invenJson.filter(
        item => !item.title.includes("이용규칙")
      );

      // 3) 사사게 글 존재 여부
      const hasIncident = realPosts.length > 0;

      // 4) 모달 오픈 + 데이터 세팅
      setData({ ...json, hasIncident });
      setOpen(true);

    } catch (err) {
      console.error(err);
      alert("조회 중 오류가 발생했습니다.");
    }
  };

  const handleSearch = (e) => {
    e.preventDefault();
    performSearch(name);
  };

  // 🔗 URL 파라미터 감지 (헤더 검색 연동)
  useState(() => {
    const params = new URLSearchParams(window.location.search);
    const queryNickname = params.get("nickname");
    if (queryNickname) {
      setName(queryNickname);
      performSearch(queryNickname);
    }
  }, []);


  return (
    <div className="page-container">
      <h2>🧙 캐릭터 검색</h2>

      {/* 기존 검색 UI 유지 */}
      <form onSubmit={handleSearch}>
        <input
          type="text"
          placeholder="캐릭터명 입력"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        <button type="submit">검색</button>
      </form>

      <div className="character-info">
        <p></p>{/* 안내할꺼 있을떄 여기다*/}
      </div>

      {/* 모달 표시 */}
      {open && (
        <CharacterSearchModal
          data={data}
          onClose={() => setOpen(false)}
          hasIncident={data?.hasIncident}
        />
      )}
    </div>
  );
}
