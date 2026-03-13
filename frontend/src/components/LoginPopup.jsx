import React, { useState } from "react";
import "../styles/login-popup.css";
import { backendHost } from "../utils/api";

function LoginPopup({ onClose, onSignup, onLoginSuccess }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();

    try {
      const form = new URLSearchParams();
      form.append("email", email);
      form.append("password", password);

      const res = await fetch(`${backendHost}/api/user/login`, {
        method: "POST",
        credentials: "include",
        body: form,
      });

      if (res.status === 200) {
        await new Promise((r) => setTimeout(r, 200));

        const sessionRes = await fetch(`${backendHost}/api/session`, {
          credentials: "include",
        });
        const sessionData = await sessionRes.json();

        if (sessionData.user) {
          alert("✅ 로그인 성공!");
          onLoginSuccess?.(sessionData.user); // 🔥 헤더/앱에 즉시 반영
          onClose();
        } else {
          alert("⚠️ 세션 정보가 확인되지 않았습니다.");
        }
      } else {
        const msg = await res.text();
        alert("❌ 로그인 실패: " + msg);
      }
    } catch (err) {
      console.error("로그인 중 오류:", err);
      alert("서버 오류가 발생했습니다.");
    }
  };

  return (
    <div className="login-popup-container" onClick={onClose}>
      <div className="login-popup" onClick={(e) => e.stopPropagation()}>
        <button className="close-btn-x" onClick={onClose}>✕</button>

        <h2>로그인</h2>
        <form onSubmit={handleSubmit}>
          <label>이메일</label>
          <input
            type="email"
            placeholder="이메일 입력"
            value={email}
            onChange={(e) => setEmail(e.target.value)}
            required
          />

          <label>비밀번호</label>
          <input
            type="password"
            placeholder="비밀번호 입력"
            value={password}
            onChange={(e) => setPassword(e.target.value)}
            required
          />

          <button type="submit" className="login-btn">로그인</button>
        </form>

        <div className="popup-actions">
          <button
            className="google-btn"
            onClick={() =>
              (window.location.href = `${backendHost}/oauth2/authorization/google`)
            }
          >
            <img
              src="/src/assets/google-icon.png"
              alt="Google"
              className="google-icon"
            />
            Google 로그인
          </button>
          <button className="signup-btn" onClick={onSignup}>회원가입</button>
        </div>
      </div>
    </div>
  );
}

export default LoginPopup;
