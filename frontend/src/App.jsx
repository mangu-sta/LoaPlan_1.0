import { BrowserRouter as Router, Routes, Route } from "react-router-dom";
import { useEffect, useState } from "react";
import Header from "./components/Header";
import Home from "./pages/Home";
import AccidentSearch from "./pages/AccidentSearch";
import Scheduler from "./pages/Scheduler";
import Community from "./pages/Community";
import CharacterSearch from "./pages/CharacterSearch";
import RaidInfo from "./pages/RaidInfo";

import "./styles/page.css";
import "./styles/common.css";
import { backendHost } from "./utils/api";

function App() {
  // 전역 로그인 상태
  const [user, setUser] = useState(null);

  // 앱 로드 시 세션 체크
  useEffect(() => {
    fetch(`${backendHost}/api/session`, { credentials: "include" })
      .then((res) => (res.ok ? res.json() : null))
      .then((data) => setUser(data?.user ?? null))
      .catch(() => setUser(null));
  }, []);

  return (
    <Router>
      <Header user={user} setUser={setUser} />

      <main>
        <Routes>
          <Route path="/" element={<Home user={user} />} />
          <Route path="/scheduler" element={<Scheduler user={user} />} />
          <Route path="/community" element={<Community user={user} />} />
          <Route path="/character/search" element={<CharacterSearch user={user} />} />
          <Route path="/raid/info" element={<RaidInfo user={user} />} />
          <Route path="/accidentSearch" element={<AccidentSearch user={user} />} />
        </Routes>
      </main>
    </Router>
  );  
}

export default App;
