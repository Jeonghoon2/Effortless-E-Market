import { useParams } from "react-router-dom";
import Header from "../LayoutComponents/Home/Header/Header";
import Footer from "../LayoutComponents/Home/Footer/Footer";
import ProductList from "../LayoutComponents/Product/ProductList/ProductList";

function ProductListPage() {
  return (
    <div>
      <div className="main-container">
        <Header />
        <ProductList />
      </div>

      <Footer />
    </div>
  );
}

export default ProductListPage;
