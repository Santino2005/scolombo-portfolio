import { BrowserRouter, Navigate, Route, Routes } from "react-router-dom";
import LoginPage from "./pages/LoginPage";
import VisitorPage from "./pages/VisitorPage";
import GuardPage from "./pages/GuardPage";
import PrivateRoute from "./components/PrivateRoute";
import { ROUTES } from "./constants/routes";
import "./styles/App.css";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path={ROUTES.LOGIN} element={<LoginPage />} />
        <Route
          path={ROUTES.VISITOR}
          element={
            <PrivateRoute>
              <VisitorPage />
            </PrivateRoute>
          }
        />
        <Route
          path={ROUTES.GUARD}
          element={
            <PrivateRoute>
              <GuardPage />
            </PrivateRoute>
          }
        />
        <Route path="*" element={<Navigate to={ROUTES.LOGIN} replace />} />
      </Routes>
    </BrowserRouter>
  );
}
