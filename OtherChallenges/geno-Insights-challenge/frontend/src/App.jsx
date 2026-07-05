import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import LoginPage from "./pages/LoginPage.jsx";
import VisitorPage from "./pages/VisitorPage";
import GuardPage from "./pages/GuardPage";
import PrivateRoute from "./components/PrivateRoute.jsx";
import "./styles/App.css";

export default function App() {
  return (
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />

          <Route path="/visitor" element={<PrivateRoute><VisitorPage /></PrivateRoute>} />
          <Route path="/guard" element={<PrivateRoute><GuardPage /></PrivateRoute>} />

          <Route path="*" element={<Navigate to="/login" />} />
        </Routes>
      </BrowserRouter>
  );
}