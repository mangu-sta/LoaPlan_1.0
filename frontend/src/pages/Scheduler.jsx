import { useState, useEffect } from "react";
import "../styles/scheduler.css";
import { backendHost } from "../utils/api";
import CharacterScheduleModal from "../components/CharacterScheduleModal";

export default function Scheduler({ user }) {
  // ===============================
  // 1. 모든 Hook은 최상단에 선언
  // ===============================
  
  // 🔍 검색 및 필터링 상태
  const [searchTerm, setSearchTerm] = useState(""); // 검색어

  // ➕ 캐릭터 추가 모달 상태
  const [showAddModal, setShowAddModal] = useState(false);
  const [newCharacterName, setNewCharacterName] = useState("");

  const [characters, setCharacters] = useState([]);
  const [loading, setLoading] = useState(true);
  const [progressMap, setProgressMap] = useState({});
  const [totalProgress, setTotalProgress] = useState({ dailyPercent: 0, weeklyPercent: 0 });

  // 경매 관련 상태
  const [itemPrice, setItemPrice] = useState("");
  const [people, setPeople] = useState(8);
  const [result, setResult] = useState(null);

  // 캐릭터 팝업 관련 상태
  const [showCharacterModal, setShowCharacterModal] = useState(false);
  const [selectedCharacter, setSelectedCharacter] = useState(null);

  // 팝업 표시 여부
  const [showAuctionModal, setShowAuctionModal] = useState(false);

  // 🔵 삭제 모드 상태 (Hook 순서 위반 해결을 위해 상단으로 이동)
  const [isDeleteMode, setIsDeleteMode] = useState(false);

  // ===============================
  // 2. Helper Functions
  // ===============================

  const openCharacterModal = (character) => {
    setSelectedCharacter(character);
    setShowCharacterModal(true);
  };

  // 모든 캐릭터의 진행도 로딩
  const fetchProgressAll = async (charList) => {
    const result = {};
    const daysMap = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"];
    const todayStr = daysMap[new Date().getDay()];

    // 전체 합계 변수
    let gDailyTotal = 0;
    let gDailyCompleted = 0;
    let gWeeklyTotal = 0;
    let gWeeklyCompleted = 0;

    for (const ch of charList) {
      const res = await fetch(
        `${backendHost}/api/schedule/list?characterId=${ch.id}`,
        { credentials: "include" }
      );
      const data = await res.json();

      // ⭐ 오늘 활성화된 태스크만 필터링
      const activeTasks = data.filter(t => {
        if (t.type === "EVENT" && t.officialDays) {
           const validDays = t.officialDays.split(",");
           return validDays.includes(todayStr); 
        }
        return true; 
      });

      // Daily / Weekly 분리 계산
      const dailyTasks = activeTasks.filter(t => t.resetType === "DAILY");
      const weeklyTasks = activeTasks.filter(t => t.resetType === "WEEKLY");

      const dailyTotal = dailyTasks.length;
      const dailyCompleted = dailyTasks.filter(t => t.isCompleted).length;
      const dailyPercent = dailyTotal === 0 ? 0 : Math.round((dailyCompleted / dailyTotal) * 100);

      const weeklyTotal = weeklyTasks.length;
      const weeklyCompleted = weeklyTasks.filter(t => t.isCompleted).length;
      const weeklyPercent = weeklyTotal === 0 ? 0 : Math.round((weeklyCompleted / weeklyTotal) * 100);

      // 전체 합계 누적
      gDailyTotal += dailyTotal;
      gDailyCompleted += dailyCompleted;
      gWeeklyTotal += weeklyTotal;
      gWeeklyCompleted += weeklyCompleted;

      result[ch.id] = {
        daily: { percent: dailyPercent },
        weekly: { percent: weeklyPercent },
      };
    }

    // 전체 퍼센트 계산
    const gDailyPercent = gDailyTotal === 0 ? 0 : Math.round((gDailyCompleted / gDailyTotal) * 100);
    const gWeeklyPercent = gWeeklyTotal === 0 ? 0 : Math.round((gWeeklyCompleted / gWeeklyTotal) * 100);

    setTotalProgress({
        dailyPercent: gDailyPercent,
        weeklyPercent: gWeeklyPercent
    });

    setProgressMap(result);
  };

  // ===============================
  // 3. useEffect Hooks
  // ===============================

  // 캐릭터 목록 자동 불러오기
  useEffect(() => {
    const fetchCharacters = async () => {
      try {
        const res = await fetch(`${backendHost}/api/characters`, {
          method: "GET",
          credentials: "include",
        });

        if (res.status === 401) {
          console.warn("🔒 로그인 필요 — 세션이 없습니다.");
          setCharacters([]);
          setLoading(false);
          return;
        }

        if (!res.ok) {
          throw new Error(`⚠️ 캐릭터 목록 불러오기 실패 (${res.status})`);
        }

        const data = await res.json();
        console.log("✅ 로그인된 계정 캐릭터 목록:", data);
        setCharacters(data);
      } catch (err) {
        console.error("❌ 캐릭터 목록 요청 실패:", err);
      } finally {
        setLoading(false);
      }
    };

    if (user) {
        fetchCharacters();
    } else {
        setLoading(false); 
    }
  }, [user]);

  useEffect(() => {
    if (user === null) {
      setCharacters([]);
      setLoading(false);
    }
  }, [user]);

  useEffect(() => {
    if (characters.length > 0) {
      fetchProgressAll(characters);
    }
  }, [characters]);

  // ===============================
  // 4. 이벤트 핸들러
  // ===============================

   // 🗑️ 캐릭터 삭제 핸들러
   const handleDeleteCharacter = async (e, charId) => {
    // ... (기존 코드 유지) ...
    e.stopPropagation(); // 카드 클릭(모달 열기) 방지

    if (window.confirm("정말로 해당 캐릭터를 삭제하시겠습니까?")) {
        try {
            const res = await fetch(`${backendHost}/api/characters/${charId}`, {
                method: "DELETE",
                credentials: "include",
            });

            if (res.ok) {
                alert("캐릭터가 삭제되었습니다.");
                // 목록 갱신
                setCharacters((prev) => prev.filter((ch) => ch.id !== charId));
            } else {
                alert("삭제 실패: " + res.status);
            }
        } catch (err) {
            console.error("삭제 중 오류:", err);
            alert("삭제 중 오류가 발생했습니다.");
        }
    }
   };

  // ✅ 캐릭터 추가 (모달에서 호출)
  const addCharacter = async () => {
    // ... (기존 코드) ... 
    if (!newCharacterName.trim()) {
        alert("닉네임을 입력해주세요.");
        return;
    }

    try {
      const res = await fetch(`${backendHost}/api/characters`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ nickname: newCharacterName }),
        credentials: "include",
      });
      
      // ❌ 404: 캐릭터 없음 등 예외 처리
      if (res.status === 404) {
          alert("존재하지 않는 캐릭터 닉네임입니다. 다시 확인해주세요.");
          return;
      }

      if (!res.ok) throw new Error(`HTTP ${res.status}`);

      const data = await res.json();
      console.log("✅ 새 캐릭터 추가:", data);

      setCharacters((prev) => [...prev, data]);
      setNewCharacterName(""); // 입력창 초기화
      setShowAddModal(false);  // 모달 닫기
      alert("캐릭터가 성공적으로 추가되었습니다!");

    } catch (err) {
      console.error("❌ 캐릭터 추가 실패:", err);
      alert("캐릭터 추가 중 오류가 발생했습니다.");
    }
  };

  // � 캐릭터 정보 새로고침
  const handleRefresh = async () => {
      if (loading) return; // 이미 로딩 중이면 무시
      setLoading(true); // 전체 로딩 표시 (또는 별도 refresh loading 상태 사용 가능)
      
      try {
          const res = await fetch(`${backendHost}/api/characters/refresh`, {
              method: "POST",
              credentials: "include",
          });
          
          if (!res.ok) throw new Error("새로고침 실패");

          const data = await res.json();
          console.log("✅ 캐릭터 정보 갱신 완료:", data);
          setCharacters(data); // 목록 업데이트
          alert("모든 캐릭터 정보가 최신으로 업데이트되었습니다.");
      } catch (err) {
          console.error("❌ 새로고침 오류:", err);
          alert("캐릭터 정보를 갱신하는 중 오류가 발생했습니다.");
      } finally {
          setLoading(false);
      }
  };

  // �🔍 검색 필터링 로직
  // ... (기존 코드) ...
  // 검색어가 없으면 전체 목록, 있으면 해당 글자가 포함된 캐릭터만 필터링
  const filteredCharacters = characters.filter((ch) => 
      !searchTerm || ch.nickname.toLowerCase().includes(searchTerm.toLowerCase())
  );


  // ... (드래그 앤 드롭, 경매 계산 로직 등 기존 코드 유지) ...
  const handleDrag = (e, index) => e.dataTransfer.setData("index", index);
  const handleDrop = async (e, targetIndex) => {
    e.preventDefault();
    const sourceIndex = Number(e.dataTransfer.getData("index"));
    
    // ⭐ 필터링 된 상태에서의 인덱스가 아니라 원본 배열 기준 인덱스를 찾아야 할 수도 있음
    // 하지만 현재 UI 구조상 필터링된 리스트를 보여주면 드래그앤드롭이 꼬일 수 있음.
    // 검색 중에는 드래그앤드롭을 막거나, 원본 인덱스를 추적해야 함.
    // 간단한 해결책: 검색어가 있을 땐 드래그 불가능하게 하거나(간단), 
    // 혹은 검색된 리스트 내에서만 순서를 바꾸는건 의미가 없으므로 (전체 순서가 중요하므로)
    // 여기서는 "검색 중이 아닐 때만" 드래그 허용하도록 처리하는 게 안전함.
    
    if (searchTerm) {
        alert("검색 중에는 순서를 변경할 수 없습니다.");
        return;
    }

    const newList = [...characters];
    const [moved] = newList.splice(sourceIndex, 1);
    newList.splice(targetIndex, 0, moved);
    setCharacters(newList);

    try {
      const orderIds = newList.map(ch => ch.id);
      await fetch(`${backendHost}/api/characters/reorder`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(orderIds),
        credentials: "include",
      });
      console.log("✅ 순서 저장 완료:", orderIds);
    } catch (err) {
      console.error("❌ 순서 저장 실패:", err);
    }
  };

  // ... (경매 계산 코드 유지) ...
  const calcAuction = () => {
      // ... (기존 코드)
      const price = Number(itemPrice);
      const n = Number(people);
      if (!price || n < 2) return;
  
      const directBid = Math.floor(price * 0.875);
      const divide = Math.floor((price * 0.875) / (n - 1));
  
      const fee = Math.floor(price * 0.05);
      const afterFee = Math.floor(price * 0.95);
      const sellSplit = Math.floor(afterFee / n);
      const sellBreakEven = afterFee - sellSplit;
      const sellProfit = fee + sellSplit;
  
      const directSell = Math.floor(price * 0.7557);
      const directSellDivide = Math.floor(directSell / (n - 1));
      const directSellProfit = price - directSell;

    setResult({
      directBid,
      divide,
      fee,
      sellSplit,
      sellBreakEven,
      sellProfit,
      directSell,
      directSellDivide,
      directSellProfit,
    });
  };

  const reset = () => {
    setItemPrice("");
    setPeople(8);
    setResult(null);
  };


  if (loading) return <div>로딩 중...</div>;
  if (!user) {
      return (
          <div className="page-container">
              <h2>📅 스케줄러</h2>
              <div className="need-login">
                  <h3>로그인이 필요한 서비스입니다</h3>
                  <p>스케줄러 기능을 사용하시려면 로그인을 해주세요.</p>
                  <p style={{fontSize: '0.9rem', color: '#999'}}>우측 상단의 로그인 버튼을 이용해주세요.</p>
              </div>
          </div>
      );
  }

  return (
    <div className="page-container">
      <h2>📅 스케줄러</h2>

      {/* 캐릭터 관리 영역 */}
      <div className="add-section">
        {/* 🔍 캐릭터 검색창 (기존 입력창 재활용) */}
        <input
          type="text"
          placeholder="캐릭터 검색..."
          value={searchTerm}
          onChange={(e) => setSearchTerm(e.target.value)}
        />
        <div>
          {/* ➕ 캐릭터 추가 버튼 (클릭 시 모달 오픈) */}
          <button onClick={() => setShowAddModal(true)} style={{marginRight: '8px'}}>캐릭터 추가</button>
          
          {/* 🔄 새로고침 버튼 */}
          <button 
            onClick={handleRefresh} 
            style={{ 
                backgroundColor: '#4facfe', 
                color: 'white', 
                marginRight: '8px',
                border: 'none'  // 기본 스타일 덮어쓰기 위해 명시
            }}
          >
            🔄 동기화
          </button>

          <button 
            onClick={() => setIsDeleteMode(!isDeleteMode)}
            className={isDeleteMode ? "delete-mode-active" : ""}
            style={{ backgroundColor: isDeleteMode ? "#FF5252" : "" }}
          >
            {isDeleteMode ? "삭제 취소" : "삭제 모드"}
          </button>
          <button onClick={() => setShowAuctionModal(true)}>경매 입찰 계산기</button>
        </div>
      </div>

      {/* 📊 전체 진행도 바 (Global Progress) */}
      <div className="global-progress-card">
          <div className="global-progress-row">
              <span className="gp-label">일일 숙제</span>
              <div className="gp-bar-bg">
                  <div 
                    className="gp-bar-fill daily" 
                    style={{ width: `${totalProgress.dailyPercent}%` }}
                  ></div>
              </div>
              <span className="gp-text">{totalProgress.dailyPercent}%</span>
          </div>

          <div className="global-progress-row">
              <span className="gp-label">주간 숙제</span>
              <div className="gp-bar-bg">
                  <div 
                    className="gp-bar-fill weekly" 
                    style={{ width: `${totalProgress.weeklyPercent}%` }}
                  ></div>
              </div>
              <span className="gp-text">{totalProgress.weeklyPercent}%</span>
          </div>
      </div>

      <div className="progress-area">
        {/* 🔍 필터링된 목록 렌더링 */}
        {filteredCharacters.map((ch, i) => (
          <div
            key={ch.id || i}
            className="character-card"
            draggable={!searchTerm} // ⭐ 검색 중에는 드래그 불가
            onClick={() => openCharacterModal(ch)}
            onDragStart={(e) => handleDrag(e, i)}
            onDrop={(e) => handleDrop(e, i)}
            onDragOver={(e) => e.preventDefault()}
            style={{ cursor: searchTerm ? 'default' : 'move' }} // 커서 스타일도 변경
          >
            <div className="char-image-wrap">
              <img
                src={ch.characterImageUrl || "/img/default-character.png"}
                alt={ch.nickname}
                className="char-image"
              />
              <div className="char-gradient-overlay"></div>
              
              {isDeleteMode && (
                <div 
                  className="delete-icon-overlay"
                  onClick={(e) => handleDeleteCharacter(e, ch.id)}
                >
                  ✖
                </div>
              )}
            </div>

            <div className="char-info">
              <div className="char-name">{ch.nickname}</div>
              <div className="char-job">{ch.className}</div>
              <div className="char-server">{ch.serverName}</div>
              <div className="char-item">Lv. {ch.itemLevel}</div>

              <div className="progress-container" style={{ marginTop: '12px' }}>
                <div className="progress-bar" style={{ height: '6px', marginBottom: '4px' }}>
                   <div
                     className="progress-fill"
                     style={{
                       width: `${progressMap[ch.id]?.daily?.percent || 0}%`,
                       background: 'linear-gradient(90deg, #4facfe, #00f2fe)'
                     }}
                   ></div>
                </div>

                <div className="progress-bar" style={{ height: '6px', marginTop: '0' }}>
                   <div
                     className="progress-fill"
                     style={{
                       width: `${progressMap[ch.id]?.weekly?.percent || 0}%`
                     }}
                   ></div>
                </div>
              </div>

              <div className="progress-text" style={{ display: 'flex', justifyContent: 'center', gap: '4px' }}>
                <span style={{ color: '#4facfe', fontWeight: 'bold' }}>
                  {progressMap[ch.id]?.daily?.percent || 0}%
                </span>
                <span style={{ color: '#888' }}>/</span>
                <span style={{ color: '#fbc02d', fontWeight: 'bold' }}>
                  {progressMap[ch.id]?.weekly?.percent || 0}%
                </span>
              </div>
            </div>
          </div>
        ))}
        {/* 검색 결과 없음 표시 */}
        {filteredCharacters.length === 0 && (
            <div style={{ padding: "2rem", color: "#ccc" }}>
                {searchTerm ? `"${searchTerm}" 검색 결과가 없습니다.` : "등록된 캐릭터가 없습니다."}
            </div>
        )}
      </div>

    {/* 기존 스케줄 모달, 경매 모달 유지... */}
          {showCharacterModal && (
            <CharacterScheduleModal
              isOpen={showCharacterModal}
              character={selectedCharacter}
              onClose={() => {
                setShowCharacterModal(false);
                fetchProgressAll(characters);
              }}
            />
          )}

      {showAuctionModal && (
        <div className="modal-overlay" onClick={() => setShowAuctionModal(false)}>
           {/* ... 경매 모달 내용 ... */}
           <div className="modal" onClick={(e) => e.stopPropagation()}>
               {/* (경매 모달 내용은 기존 코드 그대로 유지되지만, 편의상 생략된 부분은 ...로 표시) */}
               {/* ⚠️ 주의: 실제 코드 복붙 시에는 기존 내용을 그대로 유지해야 함. */}
               {/* 여기서는 replace_tool이 EndLine까지 덮어쓰므로 꼼꼼히 확인 필요 */}
             <h3>💰 경매 입찰 계산기</h3>
             <button onClick={() => setShowAuctionModal(false)} className="close-btn">✖ 닫기</button>
             <div className="auction-inputs"> 
                {/* ... 기존 auction inputs ... */}
                <div className="input-row">
                <label>템 가격</label>
                <input
                  type="number"
                  placeholder="예: 40000"
                  value={itemPrice}
                  onChange={(e) => setItemPrice(e.target.value)}
                />
                <button onClick={reset} className="reset-btn">
                  금액 초기화
                </button>
              </div>

              <div className="input-row">
                <label>인원</label>
                {[4, 8].map((num) => (
                  <label key={num} style={{ marginRight: "1rem" }}>
                    <input
                      type="radio"
                      name="people"
                      checked={people === num}
                      onChange={() => setPeople(num)}
                    />
                    {num}인
                  </label>
                ))}
              </div>

              <button onClick={calcAuction} className="calc-btn">
                계산
              </button>
             </div>
             {result && (
               <div className="auction-results">
                 <h4>📊 직접사용</h4>
                 <p>입찰 적정가: <strong>{result.directBid.toLocaleString()} G</strong></p>
                 <p>분배금: <strong>{result.divide.toLocaleString()} G</strong></p>

                 <h4>📈 판매</h4>
                 <p>수수료: <strong>{result.fee.toLocaleString()} G</strong></p>
                 <hr />
                 <p>손익분기점: <strong>{result.sellBreakEven.toLocaleString()} G</strong></p>
                 <p>분배금: <strong>{result.sellSplit.toLocaleString()} G</strong></p>
                 <p>판매차익: <strong>{result.sellProfit.toLocaleString()} G</strong></p>
                 <hr />
                 <p>입찰적정가: <strong style={{color:"#0c0"}}>{result.directSell.toLocaleString()} G</strong></p>
                 <p>분배금: <strong>{result.directSellDivide.toLocaleString()} G</strong></p>
                 <p>판매차익: <strong>{result.directSellProfit.toLocaleString()} G</strong></p>
               </div>
             )}
           </div>
        </div>
      )}

      {/* 🆕 캐릭터 추가 모달 */}
      {showAddModal && (
        <div className="modal-overlay" onClick={() => setShowAddModal(false)}>
          <div className="modal" onClick={(e) => e.stopPropagation()}>
            <h3>캐릭터 추가</h3>
            <button className="close-btn" onClick={() => setShowAddModal(false)}>✖</button>
            
            <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem', marginTop: '1rem' }}>
                <p>로스트아크 캐릭터 닉네임을 입력하세요.</p>
                <input 
                    type="text" 
                    placeholder="닉네임 입력"
                    value={newCharacterName}
                    onChange={(e) => setNewCharacterName(e.target.value)}
                    style={{ 
                        padding: '10px', 
                        borderRadius: '6px', 
                        border: '1px solid #ccc',
                        fontSize: '1rem'
                    }}
                    onKeyDown={(e) => {
                        if (e.key === 'Enter') addCharacter();
                    }}
                />
                <div style={{ display: 'flex', justifyContent: 'flex-end', gap: '10px', marginTop: '10px' }}>
                    <button 
                        onClick={() => setShowAddModal(false)}
                        style={{
                            padding: '8px 16px',
                            background: '#ccc',
                            border: 'none',
                            borderRadius: '6px',
                            cursor: 'pointer'
                        }}
                    >
                        취소
                    </button>
                    <button 
                        onClick={addCharacter}
                        style={{
                            padding: '8px 16px',
                            background: '#fbc02d',
                            border: 'none',
                            borderRadius: '6px',
                            fontWeight: 'bold',
                            cursor: 'pointer',
                            color: '#333'
                        }}
                    >
                        추가
                    </button>
                </div>
            </div>
          </div>
        </div>
      )}

    </div>
  );
}
