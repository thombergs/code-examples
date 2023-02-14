import "bootstrap/dist/css/bootstrap.min.css";
import AddUrlComponent from "./components/AddUrlComponent";
import ViewUrlComponent from "./components/ViewUrlComponent";

function App() {
  return (
    <div className="App container mt-5">
      <AddUrlComponent />
      <ViewUrlComponent />
    </div>
  );
}

export default App;
