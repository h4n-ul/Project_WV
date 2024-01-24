import axios from "axios"
import { Cookies } from "react-cookie";
import { useState, useEffect } from "react";

axios.defaults.withCredentials = true;

const Login = () => {
  const [loginId, setLoginId] = useState([]);
  const [password, setPassword] = useState([]);

  const login = async () => {
    console.log(sessionStorage.getItem("session"))
      await axios({
        url: 'http://127.0.0.1:8080/backend/artist/login',
        method: 'post',
        data:
        {
          loginId,
          password
        },
        withCredentials: true
      })
      .then((response) => {
        const cookies = new Cookies();
        console.log(response);
        const session = response.data;
        cookies.setItem("session", session);
      })
      .catch((error) => {
        console.log(error);
      })
  }
  const logout = () => {
    if (sessionStorage.getItem("session") !== undefined) {
      sessionStorage.removeItem("session");
      axios.post('http://127.0.0.1:8080/backend/artist/logout')
      .then((response) => {
        console.log(response);
      })
      .catch((error) => {
        console.log(error);
      })
    }
  }

  return(
    <div className="Login" style={{display:'flex', flexDirection: 'column'}}>
      <input
        id="loginIdBox"
        type="text"
        value={loginId}
        onChange={(event) => setLoginId(event.target.value)}
        style={{width: '300px', height: '30px', marginBottom: '20px', alignSelf: 'center'}}
      />
      <input
        id="passwordBox"
        type="password"
        value={password}
        onChange={(event) => setPassword(event.target.value)}
        style={{width: '300px', height: '30px', marginBottom: '20px', alignSelf: 'center'}}
      />
      <button onClick={login}
        style={{ marginBottom: '10px', height: '50px', width: '100px', alignSelf: 'center'}}>Login</button>

      <button onClick={logout}
        style={{ marginBottom: '10px', height: '50px', width: '100px', alignSelf: 'center'}}>Logout</button>
    </div>
  )
}

export default Login