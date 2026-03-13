import { useState, useEffect } from "react";
import "../styles/common.css";
import LoginPopup from "./LoginPopup";
import SignupPopup from "./SignupPopup";
import { backendHost } from "../utils/api";

export default function Header({ user, setUser }) {
  const [theme, setTheme] = useState(localStorage.getItem("theme") || "light");
  const [menuOpen, setMenuOpen] = useState(false);

  const [showLogin, setShowLogin] = useState(false);
  const [showSignup, setShowSignup] = useState(false);

  // 테마 저장
  useEffect(() => {
    document.body.setAttribute("data-theme", theme);
    localStorage.setItem("theme", theme);
  }, [theme]);

  // 화면 크기 변화 → 모바일 메뉴 닫기
  useEffect(() => {
    const handleResize = () => {
      if (window.innerWidth > 1080) setMenuOpen(false);
    };
    window.addEventListener("resize", handleResize);
    return () => window.removeEventListener("resize", handleResize);
  }, []);

  // 로그아웃 처리
  const doLogout = () => {
    fetch(`${backendHost}/api/session/logout`, {
      method: "POST",
      credentials: "include",
    })
      .then((res) => {
        if (res.ok) {
          alert("✅ 로그아웃 되었습니다.");
          setUser(null); // 🔥 바로 UI 반영
        } else {
          alert("⚠️ 로그아웃 처리 중 오류가 발생했습니다.");
        }
      })
      .catch(() => alert("❌ 서버와 연결할 수 없습니다."));
  };

  return (
    <>
      {/* 🧩 로그인 팝업 */}
      {showLogin && (
        <LoginPopup
          onClose={() => setShowLogin(false)}
          onSignup={() => setShowSignup(true)}
          onLoginSuccess={(loggedUser) => {
            setUser(loggedUser); // 🔥 전역 상태 업데이트 → 즉시 UI 반영
            setShowLogin(false);
          }}
        />
      )}

      {/* 🧩 회원가입 팝업 */}
      {showSignup && (
        <SignupPopup
          onClose={() => setShowSignup(false)}
          onLogin={() => {
            setShowSignup(false);
            setShowLogin(true);
          }}
        />
      )}

      {/* 🌙 오버레이 */}
      <div
        className={`menu-overlay ${menuOpen ? "active" : ""}`}
        onClick={() => setMenuOpen(false)}
      ></div>

      <header className="main-header">
        {/* 🍔 햄버거 버튼 */}
        <button
          className="hamburger"
          onClick={() => setMenuOpen(true)}
          aria-label="메뉴 열기/닫기"
        >
          <svg viewBox="0 0 24 24">
            <rect x="3" y="6" width="18" height="2" />
            <rect x="3" y="11" width="18" height="2" />
            <rect x="3" y="16" width="18" height="2" />
          </svg>
        </button>

        {/* 🪶 로고 */}
        <div className="logo" onClick={() => (window.location.href = "/")}>
          <img src="/src/assets/logo.png" alt="사이트 로고" className="logo-img" />
        </div>

        {/* 📋 메뉴 */}
        <nav className={`menu ${menuOpen ? "open" : ""}`} id="mainMenu">
          <button
            className="sidebar-close-btn"
            onClick={() => setMenuOpen(false)}
            aria-label="메뉴 닫기"
          >
            ✕
          </button>

          {/* 🌙 테마 토글 */}
          <button
            id="themeToggleSidebar"
            className="sidebar-toggle"
            onClick={() => setTheme(theme === "dark" ? "light" : "dark")}
          >
            {theme === "dark" ? "☀️" : "🌙"}
          </button>

          <a href="/scheduler"><span className="icon">🕒</span> 스케줄러</a>
          <a href="/community"><span className="icon">💬</span> 커뮤니티</a>
          <a href="/character/search"><span className="icon">🧙‍♂️</span> 캐릭터 검색</a>
          <a href="/raid/info"><span className="icon">⚔️</span> 레이드 정보</a>
          <a href="/AccidentSearch"><span className="icon">📰</span> 사사게 검색</a>

          {/* 🔽 사이드바 하단 */}
          <div className="sidebar-footer">
            <form className="search-box" action="/character/search" method="get">
              <input type="text" name="nickname" placeholder="캐릭터 검색..." />
              <button type="submit">🔍</button>
            </form>

            <div className="sidebar-login">
              {!user ? (
                <button onClick={() => setShowLogin(true)}>로그인</button>
              ) : (
                <>
                  <button onClick={doLogout}>로그아웃</button>
                  {user.role === "ADMIN" && (
                    <button onClick={() => (window.location.href = "/admin")}>
                      관리자
                    </button>
                  )}
                </>
              )}
            </div>
          </div>
        </nav>

        {/* 🌙 오른쪽 사용자 영역 */}
        <div className="user-menu">
          <form className="search-box" action="/character/search" method="get">
            <input type="text" name="nickname" placeholder="캐릭터 검색..." />
            <button type="submit">🔍</button>
          </form>

          <div className="user-actions">
            <button
              id="themeToggle"
              className="theme-toggle"
              onClick={() => setTheme(theme === "dark" ? "light" : "dark")}
            >
              {theme === "dark" ? "☀️" : "🌙"}
            </button>

            {!user ? (
              <button onClick={() => setShowLogin(true)}>로그인</button>
            ) : (
              <>
                <button onClick={doLogout}>로그아웃</button>
                {user.role === "ADMIN" && (
                  <button onClick={() => (window.location.href = "/admin")}>
                    관리자
                  </button>
                )}
              </>
            )}
          </div>
        </div>
      </header>
    </>
  );
}
