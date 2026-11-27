import { useState } from "react";
import SignUpPage from "./pages/signup.jsx";
import LoginPage from "./pages/login.jsx";

export default function App() {
  const [page, setPage] = useState("signup");

  return page === "signup" ? (
    <SignUpPage onSwitchToLogin={() => setPage("login")} />
  ) : (
    <LoginPage onSwitchToSignUp={() => setPage("signup")} />
  );
}
