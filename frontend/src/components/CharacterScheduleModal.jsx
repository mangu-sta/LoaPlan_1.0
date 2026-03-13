// src/components/CharacterScheduleModal.jsx
import { useEffect, useState } from "react";
import "../styles/CharacterScheduleModal.css";

export default function CharacterScheduleModal({ isOpen, character, onClose }) {
  const [scheduleList, setScheduleList] = useState([]);
  const [taskCatalog, setTaskCatalog] = useState([]);

  // ⭐ 체크박스 상태를 상위로 끌어올림
  const [checkState, setCheckState] = useState({});
  const enumOrder = ["DAILY", "WEEKLY", "RAID", "EVENT", "CUSTOM"];

  // ⭐ 커스텀 스케줄 입력 팝업 상태
  const [showCustomPopup, setShowCustomPopup] = useState(false);
  const [customName, setCustomName] = useState("");
  const [customResetType, setCustomResetType] = useState("DAILY");


  /* -------------------------------------------------------------
      공통 로딩함수: task 목록
  ------------------------------------------------------------- */
  const fetchTaskCatalog = () => {
    fetch("/api/task/list", {
      credentials: "include",
    })

      .then((r) => r.json())
      .then((data) => setTaskCatalog(data))
      .catch((err) => console.error("task load error:", err));
  };

  /* -------------------------------------------------------------
      공통 로딩함수: 캐릭터 스케줄 + 체크박스 초기값 구성
  ------------------------------------------------------------- */
  // ⭐ 스케줄 목록 불러오기 + 체크박스 초기값 구성
  const refreshScheduleList = async () => {
    const resp = await fetch(`/api/schedule/list?characterId=${character.id}`, {
      credentials: "include",
    });
    const data = await resp.json();

    setScheduleList(data);

    const initial = {};
    data.forEach((s) => {
      const max = s.maxPhase ?? 1;

      if (s.type === "RAID") {
        // 🔥 레이드 전용 구조: 난이도 2줄(normal/hard)
        // 기존처럼 progressPhase만 있을 경우 → 노말 기준 체크 적용 ❌
        // RAID는 NORMAL/HARD가 나뉘므로 progressPhase 기반 체크를 하면
        // 노말/하드가 중복 체크되는 문제가 발생함
        // → 기본값은 무조건 false로 초기화
        initial[s.id] = {
          normal: Array(max).fill(false),
          hard: Array(max).fill(false),
        };

        // ⭐ difficultyData 기반 복원 (NORMAL / HARD / NONE)
        // 백엔드에서 difficultyData JSON 배열을 넘겨주면,
        // 저장된 난이도에 따라 정확히 복원한다.
        if (s.difficultyData && Array.isArray(s.difficultyData)) {
          s.difficultyData.forEach((d, idx) => {
            if (d === "NORMAL") initial[s.id].normal[idx] = true;
            if (d === "HARD")   initial[s.id].hard[idx] = true;
            // NONE → 둘 다 false 유지
          });
        }

      } else {
        // 기존 DAILY / WEEKLY / EVENT / CUSTOM
        // progressPhase 만큼 체크된 상태로 초기화 (기존 로직 그대로 유지)
        initial[s.id] = Array(max)
          .fill(false)
          .map((_, idx) => idx < s.progressPhase);
      }

    });

    setCheckState(initial);
  };


  /* -------------------------------------------------------------
      모달 열릴 때 자동 로딩
  ------------------------------------------------------------- */
  useEffect(() => {
    if (!isOpen) return;
    fetchTaskCatalog();
    refreshScheduleList();
  }, [isOpen]);

  if (!isOpen) return null;

  /* -------------------------------------------------------------
      ⭐ 스케줄 추가
  ------------------------------------------------------------- */
  /* -------------------------------------------------------------
      ⭐ 스케줄 추가
  ------------------------------------------------------------- */
  const handleAddSchedule = async (task) => {
    if (task.isCustom) {
      // 팝업 열기 & 초기화
      setCustomName("");
      setCustomResetType("DAILY");
      setShowCustomPopup(true);
      return;
    }

    // 공식 스케줄
    await fetch(
      `/api/schedule/add/official?characterId=${character.id}&taskId=${task.id}`,
      { method: "POST", credentials: "include" }
    );

    refreshScheduleList();
  };

  // ⭐ 커스텀 스케줄 실제 저장
  const handleSaveCustom = async () => {
    if (!customName.trim()) {
      alert("스케줄 이름을 입력해주세요.");
      return;
    }

    await fetch(
      `/api/schedule/add/custom?characterId=${character.id}` +
        `&name=${encodeURIComponent(customName)}` +
        `&resetType=${customResetType}`,
      { method: "POST" }
    );

    setShowCustomPopup(false);
    refreshScheduleList();
  };

  /* -------------------------------------------------------------
      ⭐ 삭제 기능
  ------------------------------------------------------------- */
  const handleRemoveSchedule = async (id) => {
    await fetch(`/api/schedule/delete?id=${id}`, {
      method: "DELETE",
      credentials: "include",
    });


    // UI에서는 즉시 제거
    setScheduleList((prev) => prev.filter((s) => s.id !== id));

    // DB 기반 최신화
    refreshScheduleList();
  };

  /* -------------------------------------------------------------
      schedule + taskCatalog → type 매핑
  ------------------------------------------------------------- */
  const schedulesWithType = scheduleList.map((s) => {
    const task = taskCatalog.find((t) => t.id === s.taskId);
    return {
      ...s,
      type: task ? task.type : "CUSTOM",
      maxPhase: task ? task.maxPhase : 1,
    };
  });

  const stopProp = (e) => e.stopPropagation();

  return (
    <div className="character-schedule-modal-overlay">
      <div className="character-schedule-modal" onClick={stopProp}>
        <div className="cs-modal-close" onClick={onClose}>
          ✕
        </div>

        <div className="cs-left-column">
          <CharacterInfoPanel character={character} />

          <ScheduleListPanel
            schedules={schedulesWithType}
            onRemove={handleRemoveSchedule}
            checkState={checkState}
            setCheckState={setCheckState}
            enumOrder={enumOrder}
          />
        </div>

        <ScheduleAddPanel
          taskCatalog={taskCatalog}
          scheduleList={scheduleList}
          onAdd={handleAddSchedule}
          enumOrder={enumOrder}  
        />

        {/* ⭐ 커스텀 스케줄 입력 모달 */}
        {showCustomPopup && (
          <div className="custom-schedule-popup-overlay">
            <div className="custom-schedule-popup">
              <h3>커스텀 스케줄 추가</h3>
              
              <div className="input-group">
                <label>스케줄 이름</label>
                <input 
                  type="text" 
                  value={customName} 
                  onChange={(e) => setCustomName(e.target.value)}
                  placeholder="예: 카던 돌기"
                  autoFocus
                />
              </div>

              <div className="input-group">
                <label>초기화 주기</label>
                <div className="reset-type-buttons">
                  <button 
                    className={customResetType === "DAILY" ? "active" : ""} 
                    onClick={() => setCustomResetType("DAILY")}
                  >
                    매일 (Daily)
                  </button>
                  <button 
                    className={customResetType === "WEEKLY" ? "active" : ""} 
                    onClick={() => setCustomResetType("WEEKLY")}
                  >
                    매주 (Weekly)
                  </button>
                </div>
              </div>

              <div className="popup-actions">
                <button onClick={() => setShowCustomPopup(false)} className="cancel-btn">취소</button>
                <button onClick={handleSaveCustom} className="confirm-btn">추가</button>
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}

/* -------------------------------------------------------------
    캐릭터 정보 카드 (원본 그대로)
------------------------------------------------------------- */
function CharacterInfoPanel({ character }) {
  if (!character) return null;
  const { nickname, className, serverName, itemLevel, characterImageUrl } =
    character;

  return (
    <section className="cs-card cs-character-card">
      <div className="cs-character-grid">
        <div className="cs-character-image">
          <img src={characterImageUrl} alt={nickname} />
        </div>
        <div className="cs-character-info">
          <h2 className="cs-character-name">{nickname}</h2>
          <p>{className}</p>
          <p>{serverName}</p>
          <p>아이템 레벨: {itemLevel}</p>
        </div>
      </div>
    </section>
  );
}


/* -------------------------------------------------------------
    ⭐ 스케줄 리스트 (체크박스 DB 연동)
------------------------------------------------------------- */
function ScheduleListPanel({
  schedules,
  onRemove,
  checkState,
  setCheckState,
  enumOrder,
}) {
  const grouped = schedules.reduce((acc, s) => {
    acc[s.type] = acc[s.type] || [];
    acc[s.type].push(s);
    return acc;
  }, {});

  // 기존 DAILY/WEEKLY용 체크박스 토글 (그냥 그대로 사용)
  const toggleCheckbox = (scheduleId, index, maxPhase) => {
    setCheckState((prev) => {
      const current = prev[scheduleId] || Array(maxPhase).fill(false);
      const updated = [...current];
      updated[index] = !updated[index];

      const checkedCount = updated.filter((v) => v).length;
      
      // 🔥 FIX: Body로 전송하도록 수정 (백엔드가 @RequestBody를 요구함)
      fetch(`/api/schedule/update?id=${scheduleId}`, {
        method: "POST",
        credentials: "include", // ✅ 인증 정보 포함
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ progressPhase: checkedCount }),
      });

      return { ...prev, [scheduleId]: updated };
    });
  };

  return (
    <section className="cs-card cs-schedule-list-card">
      <h2 className="cs-section-title">스케줄</h2>

      <div className="cs-schedule-list">
        {enumOrder
          .filter((type) => grouped[type])
          .map((type) => {
            const list = grouped[type];

            return (
              <div key={type} className="cs-schedule-type-block">
                <div className="cs-schedule-type-header">{type}</div>

                {list.map((s) => {
                  // 🔥 레이드 타입이면 RaidScheduleItem 사용
                  if (s.type === "RAID") {
                    const raidDetails = s.raidDetails || [];

                    const hasHard = raidDetails.some(d => d.difficulty === "HARD");
                    return (
                      <RaidScheduleItem
                        key={s.id}
                        schedule={s}
                        hasHard={hasHard} 
                        checkState={checkState}
                        setCheckState={setCheckState}
                        onRemove={onRemove}
                      />
                    );
                  }

                  // 그 외(Daily/Weekly/Event/Custom)는 기존 UI 그대로
                  const maxPhase = s.maxPhase ?? 1;
                  const state =
                    checkState[s.id] || Array(maxPhase).fill(false);

                  // ⭐ EVENT 요일 체크 로직
                  let isEventDisabled = false;
                  if (s.type === "EVENT" && s.officialDays) {
                    const daysMap = ["SUN", "MON", "TUE", "WED", "THU", "FRI", "SAT"];
                    const todayStr = daysMap[new Date().getDay()]; // 오늘 요일 (ex: "FRI")
                    const validDays = s.officialDays.split(","); 
                    
                    if (!validDays.includes(todayStr)) {
                      isEventDisabled = true;
                    }
                  }

                  return (
                    <div key={s.id} className={`cs-schedule-item ${isEventDisabled ? "cs-event-disabled" : ""}`}>
                      <div className="cs-raid-image"></div>

                      <div className="cs-schedule-center">
                        <span className="cs-schedule-name">
                          {s.name}
                          {isEventDisabled && <span className="cs-event-off-badge"> (오늘 미출현)</span>}
                        </span>

                        <div className="cs-schedule-bottom">
                          {Array.from({ length: maxPhase }).map(
                            (_, idx) => (
                              <label
                                key={idx}
                                className={`cs-checkbox-item ${isEventDisabled ? "disabled-checkbox" : ""}`}
                              >
                                <input
                                  type="checkbox"
                                  checked={state[idx]}
                                  disabled={isEventDisabled}
                                  onChange={() =>
                                    !isEventDisabled && toggleCheckbox(s.id, idx, maxPhase)
                                  }
                                />
                              </label>
                            )
                          )}
                        </div>
                      </div>

                      <button
                        className="cs-schedule-remove"
                        onClick={() => onRemove(s.id)}
                      >
                        ✕
                      </button>
                    </div>
                  );
                })}
              </div>
            );
          })}
      </div>
    </section>
  );
}



/* -------------------------------------------------------------
    ⭐ RAID 전용 스케줄 아이템
------------------------------------------------------------- */
function RaidScheduleItem({ schedule, hasHard, checkState, setCheckState, onRemove }) {
  const maxPhase = schedule.maxPhase ?? 1;

  // 현재 상태 가져오기 (없으면 기본값 생성)
  const current =
    checkState[schedule.id] || {
      normal: Array(maxPhase).fill(false),
      hard: Array(maxPhase).fill(false),
    };

  const applyState = (next) => {
    // 1) 프론트 상태 업데이트
    setCheckState((prev) => ({
      ...prev,
      [schedule.id]: next,
    }));

    // 2) progressPhase 계산
    let cleared = 0;
    for (let i = 0; i < maxPhase; i++) {
      if (next.normal[i] || next.hard[i]) {
        cleared++;
      }
    }

    // 3) 백엔드 업데이트
    const difficultyData = [];
      for (let i = 0; i < maxPhase; i++) {
        if (next.hard[i]) difficultyData.push("HARD");
        else if (next.normal[i]) difficultyData.push("NORMAL");
        else difficultyData.push("NONE");
      }

      fetch(`/api/schedule/update?id=${schedule.id}`, {
        method: "POST",
        credentials: "include",   // ⭐ 인증 쿠키 포함 필수
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          progressPhase: cleared,
          difficultyData: difficultyData,
        }),
      });


  };

  const toggleGate = (difficulty, index) => {
    // 1. 현재 상태 복사 (Deep Copy 아님, 1단계만 복사)
    const next = {
      normal: [...current.normal],
      hard: [...current.hard],
    };

    // 2. 클릭한 난이도 토글 (True <-> False)
    const isNowChecked = !next[difficulty][index];
    next[difficulty][index] = isNowChecked;

    // 3. 만약 '체크(True)'가 되었다면, 반대쪽 난이도는 무조건 해제(False)
    if (isNowChecked) {
      if (difficulty === "normal") {
        next.hard[index] = false;
      } else if (difficulty === "hard") {
        next.normal[index] = false;
      }
    }

    // 4. 상태 적용 및 전송
    applyState(next);
  };

  return (
    <div className="cs-schedule-item">
      {/* 레이드 이미지 자리 */}
      <div className="cs-raid-image" />

      {/* 가운데 레이드 이름 + 노말/하드 2줄 */}
      <div className="cs-schedule-center raid-center">
        <span className="cs-schedule-name">{schedule.name}</span>

        {/* NORMAL 줄 */}
        <div className="raid-row">
          <div className="raid-diff-label">노말</div>
          <div className="raid-gate-list">
            {Array.from({ length: maxPhase }).map((_, idx) => (
              <div
                key={idx}
                className={
                  "raid-gate" +
                  (current.normal[idx] ? " raid-gate-on" : "")
                }
                onClick={() => toggleGate("normal", idx)}
              >
                {idx + 1}
              </div>
            
            ))}
          </div>
        </div>

        {/* HARD 줄 */}
        <div
          className={
            "raid-row" + (!hasHard ? " raid-row-disabled" : "")
          }
        >
          <div className="raid-diff-label">하드</div>
          <div className="raid-gate-list">
            {hasHard ? (
              Array.from({ length: maxPhase }).map((_, idx) => (
                <div
                  key={idx}
                  className={
                    "raid-gate" +
                    (current.hard[idx] ? " raid-gate-on" : "")
                  }
                  onClick={() => toggleGate("hard", idx)}
                >
                  {idx + 1}
                </div>

              ))
            ) : (
              <div className="raid-no-hard">하드 없음</div>
            )}
          </div>
        </div>
      </div>

      {/* 삭제 버튼 */}
      <button
        className="cs-schedule-remove"
        onClick={() => onRemove(schedule.id)}
      >
        ✕
      </button>
    </div>
  );
}



/* -------------------------------------------------------------
    스케줄 추가 (원본 유지)
------------------------------------------------------------- */
function ScheduleAddPanel({ taskCatalog, scheduleList, onAdd, enumOrder }) {
  const isDisabledTask = (task) => {
    if (task.allowMultiple) return false;
    return scheduleList.some((s) => s.taskId === task.id);
  };


  const customTask = {
    id: 999,
    name: "커스텀 스케줄",
    type: "CUSTOM",
    allowMultiple: true,
    isCustom: true,
  };

  const allTasks = [...taskCatalog, customTask];

  const grouped = enumOrder.reduce((acc, type) => {
    acc[type] = allTasks.filter((t) => t.type === type);
    return acc;
  }, {});

  const [openCategory, setOpenCategory] = useState(null);

  return (
    <section className="cs-card cs-schedule-add-card">
      <h2 className="cs-section-title">스케줄 추가</h2>

      <div className="cs-task-group-wrapper">
        {enumOrder.map((type) => (
          <div key={type} className="cs-accordion-block">
            <button
              className="cs-accordion-header"
              onClick={() =>
                setOpenCategory((prev) => (prev === type ? null : type))
              }
            >
              <span>{type}</span>
              <span>{openCategory === type ? "▲" : "▼"}</span>
            </button>

            {openCategory === type && (
              <div className="cs-accordion-content">
                {grouped[type].map((task) => {
                  const disabled = isDisabledTask(task);
                  return (
                    <button
                      key={task.id}
                      className={
                        "cs-task-add-item" +
                        (disabled ? " cs-task-add-item-disabled" : "")
                      }
                      onClick={() => !disabled && onAdd(task)}
                      type="button"
                    >
                      <span>{task.name}</span>
                      {!task.allowMultiple && <small> (1회)</small>}
                      {task.isCustom && <small> (커스텀)</small>}
                    </button>
                  );
                })}
              </div>
            )}
          </div>
        ))}
      </div>
    </section>
  );
}
