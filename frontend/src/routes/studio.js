import logo from '../logo.svg';
import axios from "axios"
import '../App.css';

const Studio = () => {
  console.log(sessionStorage.getItem("session"))
  axios({
    url: 'http://127.0.0.1:8080/backend/test',
    method: 'get',
    withCredentials: true
  })
  .then((response) => {
    console.log(response);
  })
  .catch((error) => {
    console.log(error);
  })
  return (
    <div className="App">
      <header className="App-header">
        <img src={logo} className="App-logo" alt="logo" />
        <p>
          Edit <code>src/App.js</code> and save to reload.
        </p>
        <a
          className="App-link"
          href="https://reactjs.org"
          target="_blank"
          rel="noopener noreferrer"
        >
          Learn React
        </a>
      </header>
    </div>
  );
}

export default Studio;