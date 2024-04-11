import Grid from "./components/Grid";
import Header from "./components/Header";
import "./styles/index.scss";

function App() {
  return (
    <>
      <div className="sidebar"></div>
      <Header />
      <Grid />
    </>
  );
}

export default App;
