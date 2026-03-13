import { useEffect, useState } from "react";
import "../styles/alert-modal.css";

export default function UpdateModal() {
  const [visible, setVisible] = useState(false);

  useEffect(() => {
    // ✅ 첫 방문 시 자동 표시 (localStorage로 중복 방지)
    const seen = localStorage.getItem("seenUpdateModal");
    if (!seen) {
      setVisible(true);
      // 하루 1회만 표시하고 싶으면 아래 주석 해제
      // localStorage.setItem("seenUpdateModal", "true");
    }
  }, []);

  if (!visible) return null;

  return (
    <div className="alert-overlay">
      <div
        className="alert-modal update-modal"
        onClick={(e) => e.stopPropagation()} // ✅ 바깥 클릭 방지
      >
        <h2 className="update-title">🗞️ LoaPlan 업데이트 안내</h2>
        <div className="update-content">
          <ul>
               <div></div>
                 <li>📖 빰빠빠밤 - - -
                    <ul className="sub-list">
                      <li> 로그인 해야되요!</li> 
                      <li> abc@naver.com</li> 
                      <li> @Qwer1234</li> 
                      <li> 참고 : 미완성 입니다!</li> 
                    </ul>
                  </li>
               <div></div>
            
            <br></br>
           
            
          </ul>
        </div>

        <button
          className="alert-btn update-close"
          onClick={() => setVisible(false)}
        >
          닫기
        </button>
      </div>
    </div>
  );
}
