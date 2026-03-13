import { useEffect, useRef, useState } from "react";
import { useNavigate } from "react-router-dom"; 
import "../styles/index.css";
import { backendHost } from "../utils/api";

import UpdateModal from "../components/UpdateModal";


export default function Home() {
  const [islands, setIslands] = useState([]);
  const [nowEpochMs, setNowEpochMs] = useState(0);
  const [index, setIndex] = useState(0);
  const navigate = useNavigate(); 

  // ✅ 서버-클라 시간차를 고정값으로 유지
  const driftRef = useRef(0);
  useEffect(() => {
    if (nowEpochMs) {
      driftRef.current = Date.now() - nowEpochMs;
    }
  }, [nowEpochMs]);

  // ✅ API 호출 (반드시 상대경로 사용하면 프록시 적용됨: /api/...)
  useEffect(() => {
    fetch(`${backendHost}/api/islands/today`)
      .then((res) => res.json())
      .then((data) => {
        setIslands(data.adventureIslands || []);
        setNowEpochMs(data.nowEpochMs || 0);
      })
      .catch((err) => console.error("❌ 섬 데이터 불러오기 실패:", err));
  }, []);

  // ====== 유틸 ======
  const toDate = (raw) => {
    const [date, time] = raw.split("T");
    const [y, m, d] = date.split("-").map(Number);
    const [hh, mm, ss] = time.split(":").map(Number);
    return new Date(y, m - 1, d, hh, mm, ss);
  };

  // ====== 모험의 섬 타이머 ======
  const [remaining, setRemaining] = useState("00:00:00");

  useEffect(() => {
    if (!islands.length || !islands[index]) return;

    const tick = () => {
      const island = islands[index]; // 최신 참조
      const now = new Date(Date.now() - driftRef.current);

      let next = null;
      for (const t of island.startTimes || []) {
        const d = toDate(t);
        if (d > now) {
          next = d;
          break;
        }
      }

      if (!next) {
        setRemaining("오늘 일정 종료");
        return;
      }

      const diff = Math.floor((next - now) / 1000);
      const h = String(Math.floor(diff / 3600)).padStart(2, "0");
      const m = String(Math.floor((diff % 3600) / 60)).padStart(2, "0");
      const s = String(diff % 60).padStart(2, "0");
      setRemaining(`${h}:${m}:${s}`);
    };

    tick();
    const timer = setInterval(tick, 1000);
    return () => clearInterval(timer);
  }, [islands, index]); // ✅ drift 제거

  // ====== 필드보스 / 카게 ======
  const [bossTime, setBossTime] = useState({ next: "--:--", remain: "00:00:00" });
  const [gateTime, setGateTime] = useState({ next: "--:--", remain: "00:00:00" });

  useEffect(() => {
    const fieldDays = [0, 2, 5];
    const gateDays  = [0, 1, 4, 6];

    const getLoaDay = (date) => {
      // 로스트아크 하루 기준: 오전 06:00 ~ 익일 05:59
      const shifted = new Date(date.getTime() - 6 * 60 * 60 * 1000);
      return shifted.getDay();
    };

    const getNextEvent = (minuteMark, activeDays) => {
      const now = new Date();
      
      // 1. 현재 로아 요일 구하기
      const todayLoa = getLoaDay(now);

      // 2. 오늘 등장하는 날이 아니면 즉시 null 반환 (다음 날짜까지 탐색 X)
      if (!activeDays.includes(todayLoa)) return null;

      // 3. 탐색 한계점 설정: "내일 오전 06:00" 까지
      //    (현재가 로아 기준 '오늘'이라면, 실제 시간으로 내일 오전 6시가 '오늘'의 끝)
      //    예: 현재 1.18(토) 23:00 -> 로아 토요일. 끝은 1.19(일) 06:00
      //    예: 현재 1.19(일) 01:00 -> 로아 토요일. 끝은 1.19(일) 06:00
      const limit = new Date(now);
      if (limit.getHours() < 6) {
        // 이미 0~5시라면, 오늘 날짜의 06:00이 한계
        limit.setHours(6, 0, 0, 0);
      } else {
        // 6시 이후라면, 내일 날짜의 06:00이 한계
        limit.setDate(limit.getDate() + 1);
        limit.setHours(6, 0, 0, 0);
      }

      let next = new Date(now);
      next.setSeconds(0);
      next.setMilliseconds(0);
      next.setMinutes(minuteMark);

      // 이미 분이 지났으면 다음 시간대부터 탐색
      if (now >= next) {
        next.setHours(next.getHours() + 1);
      }

      // 4. 한계 시간 전까지만 탐색
      while (next < limit) {
        const h = next.getHours();

        // ❌ 로아는 06:00 시에는 컨텐츠가 없음 (07:00 ~ 익일 05:00)
        // 사용자가 "06시엔 아무것도 등장하지 않음"이라고 명시함.
        if (h !== 6) {
           return next;
        }
        next.setHours(next.getHours() + 1);
      }
      
      // 오늘 남은 시간 내에 등장 시간이 없다면 (예: 05:30에 확인 시)
      return null;
    };

    const formatDiff = (target) => {
      if (!target) return "";
      const now = new Date();
      const diff = Math.floor((target - now) / 1000);
      if (diff <= 0) return "00:00:00";
      const h = String(Math.floor(diff / 3600)).padStart(2, "0");
      const m = String(Math.floor((diff % 3600) / 60)).padStart(2, "0");
      const s = String(diff % 60).padStart(2, "0");
      return `${h}:${m}:${s}`;
    };

    const formatClock = (date) => {
      if (!date) return "-";
      const hh = String(date.getHours()).padStart(2, "0");
      const mm = String(date.getMinutes()).padStart(2, "0");
      return `${hh}:${mm}`;
    };

    const updateTimers = () => {
      // 필드보스
      const nextBoss = getNextEvent(3, fieldDays);
      if (nextBoss) {
        setBossTime({ next: formatClock(nextBoss), remain: formatDiff(nextBoss) });
      } else {
        // 오늘 일정이 아예 없거나, 오늘 일정이 다 끝난 경우
        setBossTime({ next: "-", remain: "오늘 일정 종료/휴무" });
      }

      // 카오스게이트
      const nextGate = getNextEvent(0, gateDays);
      if (nextGate) {
        setGateTime({ next: formatClock(nextGate), remain: formatDiff(nextGate) });
      } else {
        setGateTime({ next: "-", remain: "오늘 일정 종료/휴무" });
      }
    };

    updateTimers();
    const id = setInterval(updateTimers, 1000);
    return () => clearInterval(id);
  }, []);

  // ====== 🖼️ 렌더링 ======
  return (
    <div className="main-content">
      {/* 🔸 메인 그리드 */}
       <section>
        <div className="sub-grid">
           {/* 스케줄러 (Hero Block) */}
          <div
            className="block scheduler"
            onClick={() => navigate("/scheduler")}
          >
            <h2>📅 스케줄러</h2>
          </div>

          <div className="block" onClick={() => navigate("/community")}>
            <h2>💭 커뮤니티</h2>
          </div>
          <div className="block" onClick={() => navigate("/character/search")}>
            <h2>🧙‍♂️ 캐릭터 검색</h2>
          </div>
          <div className="block" onClick={() => navigate("/raid/info")}>
            <h2>⚔️ 콘텐츠 정보</h2>
          </div>
          <div className="block" onClick={() => navigate("/accidentSearch")}>
            <h2>📰 사사게 검색</h2>
          </div>
        </div>
      </section>

      {/* 🔸 타이머 영역 */}
      <section className="timer-section">
        <h3>게임 내 콘텐츠 타이머</h3>

        <div className="timer-grid">
          {/* 모험의 섬 */}
            {/* 모험의 섬 */}
            <div className="island-card">
              <h4>오늘의 모험섬</h4>

              {!islands.length ? (
                <div className="island-empty">오늘 등장 예정인 모험의 섬이 없습니다.</div>
              ) : (
                <>
                  {/* 기준 날짜 */}
                  <p className="island-date">
                    {islands[index]?.startTimes?.[0]?.replace("T", " ")}
                  </p>

                  {/* 섬 정보 (기존 inner card 내용 평탄화) */}
                  <div style={{ flex: 1, display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
                    <h5 className="island-name">{islands[index]?.name}</h5>

                    <div className="island-rewards">
                      <p className="reward-title" style={{ fontWeight: 'bold', marginBottom: '4px', color: '#555' }}>기대 보상</p>
                      <ul>
                        {islands[index]?.rewards?.map((r, i) => (
                          <li key={i}>{r}</li>
                        ))}
                      </ul>
                    </div>

                    <div className="island-timer">
                      <strong>{remaining}</strong>
                    </div>
                  </div>

                  {/* 좌우 버튼 */}
                  <div className="island-nav" style={{ marginTop: '10px' }}>
                    <span
                      className="nav-arrow"
                      onClick={() => setIndex((index - 1 + islands.length) % islands.length)}
                    >
                      ⟨
                    </span>
                    <span
                      className="nav-arrow"
                      onClick={() => setIndex((index + 1) % islands.length)}
                    >
                      ⟩
                    </span>
                  </div>
                </>
              )}
            </div>


          {/* 필드보스 */}
          <div className="timer-card">
            <h4>필드보스</h4>
            <div>
              <span className="fw-semibold">다음 등장:</span> {bossTime.next}
            </div>
            <div className="timer my-2">{bossTime.remain}</div>
          </div>

          {/* 카오스게이트 */}
          <div className="timer-card">
            <h4>카오스<br />게이트</h4>
            <div>
              <span className="fw-semibold">다음 등장:</span> {gateTime.next}
            </div>
            <div className="timer my-2">{gateTime.remain}</div>
          </div>
        </div>
      </section>


{/* 🔸 노션 사이드 박스 */}
<a
  href="https://www.notion.so/2a3472cef9e48089844ffbf599440169?v=2a3472cef9e480c3ad43000c6c49190a&source=copy_link" // 👉 노션 링크로 교체
  target="_blank"
  rel="noopener noreferrer"
  className="notion-sidebox"
>
  <div className="notion-inner">
    🧭 Notion 히스토리
  </div>
</a>

    <div>
      <UpdateModal /> {/* ✅ 페이지 진입 시 자동 표시 */}
    </div>



    </div>
  );
}
