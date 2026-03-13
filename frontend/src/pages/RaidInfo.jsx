import { useEffect, useState } from "react";
import "../styles/raid-info.css"; // 스타일 분리

export default function RaidInfo() {
  const [raids, setRaids] = useState([]);

  useEffect(() => {
    loadRaids();
  }, []);

  const loadRaids = async () => {
    try {
      const res = await fetch("/api/raid/list");
      const data = await res.json();
      setRaids(data);
    } catch (err) {
      console.error("raid load error:", err);
    }
  };

  return (
    <div className="page-container">
      <h2>⚔️ 레이드 정보</h2>
      <p className="page-desc">관문별 골드 및 난이도 정보</p>

      <div className="raid-grid">
        {raids.map((raid) => (
          <div key={raid.taskId} className="raid-card">
            <h3>{raid.name}</h3>

            <div className="difficulty-block">
              <h4>NORMAL</h4>
              <ul>
                {raid.details
                  .filter((d) => d.difficulty === "NORMAL")
                  .sort((a, b) => a.phase - b.phase)
                  .map((d) => (
                    <li key={d.phase}>
                      {d.phase}관문 :{" "}
                      <span className="gold">{d.gold.toLocaleString()} G</span>
                    </li>
                  ))}
              </ul>
            </div>

            <div className="difficulty-block">
              <h4>HARD</h4>
              <ul>
                {raid.details
                  .filter((d) => d.difficulty === "HARD")
                  .sort((a, b) => a.phase - b.phase)
                  .map((d) => (
                    <li key={d.phase}>
                      {d.phase}관문 :{" "}
                      <span className="gold">{d.gold.toLocaleString()} G</span>
                    </li>
                  ))}
              </ul>
            </div>
          </div>
        ))}
      </div>
    </div>
  );
}
