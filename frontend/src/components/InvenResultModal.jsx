import "../styles/invenModal.css";

export default function InvenResultModal({
  results,
  loading,
  loaderRef,
  keyword,
  onClose,
}) {
  return (
    <div className="inven-modal-overlay">
      {/* 🔥 overlay 클릭해도 닫히지 않음 */}

      <div className="inven-modal">
        
        {/* 🔥 고정 헤더 */}
        <div className="inven-modal-header">
            <h3>
            🔍 <span className="search-term">"{keyword}"</span> 검색 결과
            </h3>
          <button className="close-btn" onClick={onClose}>
            닫기
          </button>
        </div>

        {/* 리스트 스크롤 영역 */}
        <div className="result-list">
          {results.length === 0 && !loading && (
            <p>검색 결과가 없습니다.</p>
          )}

          {results.map((item, i) => (
            <a
              key={i}
              href={item.link}
              target="_blank"
              rel="noreferrer"
              className="result-item"
            >
              <h4>{item.title}</h4>
              <div className="meta">
                👤 {item.writer} | 📅 {item.date}
              </div>
            </a>
          ))}

          {/* 무한 스크롤 센티널 */}
          <div ref={loaderRef} style={{ height: "40px" }} />

          {loading && <p>불러오는 중...</p>}
        </div>
      </div>
    </div>
  );
}
