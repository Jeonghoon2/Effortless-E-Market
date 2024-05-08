import "./TopNav.css";
import { embedDashboard } from "@superset-ui/embedded-sdk";
import { useEffect } from "react";

function TopNav() {
  const getToken = async () => {
    const response = await fetch("/guest-token");
    const token = await response.json();
    return token;
  };

  useEffect(() => {
    const embed = async () => {
      await embedDashboard({
        id: "6e49abbb-533c-4b45-b1e9-92d657297b5e", // given by the Superset embedding UI
        supersetDomain: "http://10.8.0.6:8088",
        mountPoint: document.getElementById("dashboard"), // html element in which iframe render
        fetchGuestToken: () => getToken(),
        dashboardUiConfig: {
          hideTitle: true,
          hideChartControls: true,
          hideTab: true,
        },
      });
    };
    if (document.getElementById("dashboard")) {
      embed();
    }
  }, []);

  return (
    <div className="top-nav-section">
      <div className="top-nav-content-box">
        <a href="/refund/day">일 매출</a>
        <a href="/refund/day">월 매출</a>
        <a href="/refund/day">년 매출</a>
      </div>

      <div className="chart">
        <div id="dashboard"></div>
      </div>
    </div>
  );
}

export default TopNav;
