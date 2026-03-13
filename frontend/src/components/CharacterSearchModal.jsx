import "../styles/CharacterSearchModal.css";

export default function CharacterSearchModal({ data, onClose, hasIncident = false }) {

  const profile = data.profile;
  const equipment = data.equipment || [];

  const slots = ["무기", "투구", "어깨", "상의", "하의", "장갑"];
  const filtered = equipment
    .filter((e) => slots.includes(e.Type))
    .sort((a, b) => slots.indexOf(a.Type) - slots.indexOf(b.Type));

  const stats = Array.isArray(profile.Stats) ? profile.Stats : [];

  return (
    <div className="char-modal-overlay">
      <div className="char-modal">
        {/* 닫기 버튼 */}
        <button className="char-close-btn" onClick={onClose}>
          ✕
        </button>

        {/* ✅ 닉네임 + 칭호 헤더 (모달 상단 중앙) */}
        <div className="char-header">
          <h2 className={`char-name ${hasIncident ? "danger-name" : ""}`}>
            {profile.CharacterName}
          </h2>
          {profile.Title && <p className="char-title">{profile.Title}</p>}
        </div>

        {/* 상단 3단 레이아웃 */}
        <div className="char-top">
          {/* 왼쪽: 이미지 */}
          <div className="char-img-wrap">
            <img
              src={profile.CharacterImage}
              alt={profile.CharacterName}
              className="char-image"
            />
          </div>

          {/* 가운데: 정보 (Grid로 변경) */}
          <div className="char-info-grid">
            <div className="info-item">
              <span className="label">원정대</span>
              <span className="value">{profile.ExpeditionLevel}</span>
            </div>
            <div className="info-item">
              <span className="label">전투 Lv</span>
              <span className="value">{profile.CharacterLevel}</span>
            </div>
            <div className="info-item highlight">
              <span className="label">아이템 Lv</span>
              <span className="value">{profile.ItemAvgLevel}</span>
            </div>
            <div className="info-item">
              <span className="label">서버</span>
              <span className="value">{profile.ServerName}</span>
            </div>
            <div className="info-item">
              <span className="label">직업</span>
              <span className="value">{profile.CharacterClassName}</span>
            </div>
            <div className="info-item">
              <span className="label">길드</span>
              <span className="value">{profile.GuildName || "-"}</span>
            </div>
             <div className="info-item">
              <span className="label">전투력</span>
              <span className="value">{profile.CombatPower}</span>
            </div>
          </div>

          {/* 오른쪽: 스탯 */}
          <div className="stat-box">
            <h3>스탯</h3>
            <div className="stat-grid">
              {stats.slice(0, 6).map((s) => (
                <div key={s.Type} className="stat-row">
                  <span className="stat-label">{s.Type}</span>
                  <span className="stat-value">{s.Value}</span>
                </div>
              ))}
            </div>
          </div>
        </div>

        {/* 하단: 장비 전체 */}
        <div className="equip-box">
          <h3>장비 목록</h3>
          <div className="equip-grid">
            {filtered.map((item) => (
              <div className="equip-card" key={item.Type}>
                <img src={item.Icon} className="equip-icon" alt={item.Name} />
                <div className="equip-text">
                  <div className="equip-type">{item.Type}</div>
                  <div className="equip-name">
                     <span className="grade-badge">{item.Grade}</span> {item.Name}
                  </div>
                </div>
              </div>
            ))}
          </div>
        </div>
      </div>
    </div>
  );
}
