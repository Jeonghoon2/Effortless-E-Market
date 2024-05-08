import Contents from "../LayoutComponents/Home/Contents/Contents";
import Footer from "../LayoutComponents/Home/Footer/Footer";
import Header from "../LayoutComponents/Home/Header/Header";
import "./HomePage.css";
function HomePage() {
  return (
    <div>
      <div className="main-container">
        <Header />
        <Contents />
      </div>

      <Footer />
    </div>
  );
}

export default HomePage;
