import "./Contents.css";
import CategoryList from "./CategoryList/CategoryList";
import CategortPopular from "./CategortPopular/CategortPopular";
import LatestProducts from "./LatestProducts/LatestProducts";
import LatestViews from "./LatestViews/LatestViews";
function Contents() {
  return (
    <div className="content-section">
      <div className="content-container">
        <div className="left-container">
          <CategoryList />
          <CategortPopular />
        </div>

        <div className="right-container">
          <LatestProducts />
          <LatestViews />
        </div>
      </div>
    </div>
  );
}

export default Contents;
