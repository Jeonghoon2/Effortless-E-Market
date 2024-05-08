import { BrowserRouter as Router, Route, Routes } from "react-router-dom";

import HomePage from "./PageComponents/HomePage";
import LoginPage from "./PageComponents/LoginPage";
import RegisterPage from "./PageComponents/RegisterPage";
import AdminPage from "./PageComponents/AdminPage";
import "./App.css";
import ProductListPage from "./PageComponents/ProductListPage";
import ProductDetailPage from "./PageComponents/ProductDetailPage";

export default function App() {
  return (
    <div style={{ backgroundColor: "#F3F3F7" }}>
      <Router>
        <Routes>
          <Route path="/" element={<HomePage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/admin" element={<AdminPage />} />
          <Route path="/products" element={<ProductListPage />} />
          <Route path="/products/:categoryId" element={<ProductListPage />} />
          <Route path="/product/:productId" element={<ProductDetailPage />} />
        </Routes>
      </Router>
    </div>
  );
}
