import Footer from "../LayoutComponents/Home/Footer/Footer";
import Header from "../LayoutComponents/Home/Header/Header";
import ProductDetail from "../LayoutComponents/Product/ProductDetail/ProductDetail";
import "./HomePage.css";
function ProductDetailPage() {
  return (
    <div>
      <div className="main-container">
        <Header />
        <ProductDetail />
      </div>
      <Footer />
    </div>
  );
}

export default ProductDetailPage;
