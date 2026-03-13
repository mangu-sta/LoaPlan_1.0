import React, { useState } from "react";
import "../styles/signup-popup.css";
import { backendHost } from "../utils/api";

function SignupPopup({ onClose, onLogin }) {
  const [formData, setFormData] = useState({
    email: "",
    nickname: "",
    password: "",
    confirmPassword: "",
  });

  // 기존 이메일 인증 유지
  const [emailVerified, setEmailVerified] = useState(false);
  const [verificationCode, setVerificationCode] = useState("");

  // 신규 기능: 중복 체크
  const [emailCheck, setEmailCheck] = useState(null); // true / false / null
  const [nicknameCheck, setNicknameCheck] = useState(null);

  // 약관 동의
  const [agreeTerms, setAgreeTerms] = useState(false);

  const handleChange = (e) => {
    setFormData({ ...formData, [e.target.name]: e.target.value });

    // 입력 시 중복체크 리셋
    if (e.target.name === "email") setEmailCheck(null);
    if (e.target.name === "nickname") setNicknameCheck(null);
  };

  // -----------------------------
  // 🔍 이메일 중복체크
  // -----------------------------
  const checkEmail = async () => {
    if (!formData.email) return alert("이메일을 입력해주세요.");

    const res = await fetch(
      `${backendHost}/api/user/check-email?email=${formData.email}`
    );
    const data = await res.json();

    if (data.available) {
      alert("✔ 사용 가능한 이메일입니다.");
      setEmailCheck(true);
    } else {
      alert("❌ 이미 사용 중인 이메일입니다.");
      setEmailCheck(false);
    }
  };

  // -----------------------------
  // 🔍 닉네임 중복체크
  // -----------------------------
  const checkNickname = async () => {
    if (!formData.nickname) return alert("닉네임을 입력해주세요.");

    const res = await fetch(
      `${backendHost}/api/user/check-nickname?nickname=${formData.nickname}`
    );
    const data = await res.json();

    if (data.available) {
      alert("✔ 사용 가능한 닉네임입니다.");
      setNicknameCheck(true);
    } else {
      alert("❌ 이미 사용 중인 닉네임입니다.");
      setNicknameCheck(false);
    }
  };

  // -----------------------------
  // 🔐 기존 이메일 인증
  // -----------------------------
  const sendCode = async () => {
    if (!formData.email) {
      alert("이메일을 입력해주세요.");
      return;
    }

    if (emailCheck !== true) {
      return alert("이메일 중복확인을 먼저 완료해주세요.");
    }

    try {
      const res = await fetch(`${backendHost}/api/user/email/send`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email: formData.email }),
      });

      const data = await res.json();
      if (res.ok) {
        alert("📩 인증 코드가 발송되었습니다.\n임시코드: " + data.tempCode);
      } else {
        alert("⚠ 이메일 전송 실패");
      }
    } catch (err) {
      console.error("sendCode error:", err);
      alert("서버 오류");
    }
  };

  const verifyCode = async () => {
    if (!verificationCode) return alert("인증코드를 입력해주세요.");

    try {
      const res = await fetch(`${backendHost}/api/user/email/verify`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          email: formData.email,
          code: verificationCode,
        }),
      });

      const data = await res.json();
      if (data.verified) {
        alert("✔ 이메일 인증 완료!");
        setEmailVerified(true);
      } else {
        alert("❌ 인증 코드가 올바르지 않습니다.");
      }
    } catch (err) {
      console.error("verifyCode error:", err);
      alert("서버 오류");
    }
  };

  // -----------------------------
  // 🔐 비밀번호 정규식 검사(특수문자 포함)
  // -----------------------------
  const validatePassword = () => {
    const regex =
    /^(?=.*[A-Za-z])(?=.*\d)(?=.*[^A-Za-z0-9]).{8,20}$/;

    return regex.test(formData.password);
  };

  // -----------------------------
  // 최종 회원가입 제출
  // -----------------------------
  const handleSubmit = async (e) => {
    e.preventDefault();

    if (!emailVerified) return alert("이메일 인증을 완료해주세요.");
    if (emailCheck !== true) return alert("이메일 중복확인을 완료해주세요.");
    if (nicknameCheck !== true) return alert("닉네임 중복확인을 완료해주세요.");
    if (!validatePassword())
      return alert("비밀번호는 8~20자, 영문+숫자+특수문자 포함해야 합니다.");
    if (formData.password !== formData.confirmPassword)
      return alert("비밀번호가 서로 일치하지 않습니다.");
    if (!agreeTerms) return alert("약관에 동의해야 합니다.");

    try {
      const res = await fetch(`${backendHost}/api/user/join`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          email: formData.email,
          nickname: formData.nickname,
          password: formData.password,
        }),
      });

      if (res.ok) {
        alert("🎉 회원가입이 완료되었습니다!");
        onClose();
      } else {
        alert("회원가입 실패");
      }
    } catch (err) {
      console.error("join error:", err);
      alert("서버 오류가 발생했습니다.");
    }
  };

  return (
    <div className="signup-popup-container" onClick={onClose}>
      <div className="signup-popup" onClick={(e) => e.stopPropagation()}>
        <button className="close-btn-x" onClick={onClose}>
          ✕
        </button>

        <h2>회원가입</h2>

        <form onSubmit={handleSubmit}>
          {/* 이메일 */}
          <label>이메일</label>
          <div style={{ display: "flex", gap: "0.4rem" }}>
            <input
              type="email"
              name="email"
              placeholder="이메일 입력"
              value={formData.email}
              onChange={handleChange}
              required
              disabled={emailVerified}
            />
            <button type="button" onClick={checkEmail}>
              중복확인
            </button>
            <button type="button" onClick={sendCode} disabled={emailVerified}>
              코드전송
            </button>
          </div>

          {/* 인증 코드 */}
          {!emailVerified && (
            <>
              <label>인증 코드</label>
              <div style={{ display: "flex", gap: "0.4rem" }}>
                <input
                  type="text"
                  placeholder="6자리 코드 입력"
                  value={verificationCode}
                  onChange={(e) => setVerificationCode(e.target.value)}
                />
                <button type="button" onClick={verifyCode}>
                  확인
                </button>
              </div>
            </>
          )}

          {/* 닉네임 */}
          <label>닉네임</label>
          <div style={{ display: "flex", gap: "0.4rem" }}>
            <input
              type="text"
              name="nickname"
              placeholder="닉네임 입력"
              value={formData.nickname}
              onChange={handleChange}
              required
            />
            <button type="button" onClick={checkNickname}>
              중복확인
            </button>
          </div>

          {/* 비밀번호 */}
          <label>비밀번호 (8~20자, 영문+숫자+특수문자)</label>
          <input
            type="password"
            name="password"
            placeholder="비밀번호 입력"
            value={formData.password}
            onChange={handleChange}
            required
          />

          <label>비밀번호 확인</label>
          <input
            type="password"
            name="confirmPassword"
            placeholder="비밀번호 확인"
            value={formData.confirmPassword}
            onChange={handleChange}
            required
          />

          {/* 약관동의 */}
          <div style={{ marginTop: "1rem" }}>
            <label>
              <input
                type="checkbox"
                checked={agreeTerms}
                onChange={(e) => setAgreeTerms(e.target.checked)}
              />{" "}
              (필수) 약관에 동의합니다
            </label>
          </div>

          {/* 회원가입 버튼 */}
          <button
            type="submit"
            className="signup-btn-main"
            disabled={!emailVerified}
            style={{
              opacity: emailVerified ? 1 : 0.6,
              cursor: emailVerified ? "pointer" : "not-allowed",
            }}
          >
            회원가입
          </button>
        </form>

        <div className="popup-actions">
          <button className="login-switch-btn" onClick={onLogin}>
            이미 계정이 있으신가요? 로그인
          </button>
        </div>
      </div>
    </div>
  );
}

export default SignupPopup;
