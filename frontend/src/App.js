import Hall from "./routes/hall";
import Header from "./routes/header";
import Login from "./routes/login";
import Mixtape from "./routes/mixtape";
import Produce from "./routes/produce";
import Register from "./routes/register";
import Studio from "./routes/studio";
import { BrowserRouter, Routes, Route } from 'react-router-dom';

function App() {
  return (
    <div className='App'>
      <BrowserRouter>
        <Header/>
        <Routes>
          <Route path='/' element={<Studio/>}></Route>
          <Route path='login' element={<Login/>}></Route>
          <Route path='register' element={<Register/>}></Route>
          <Route path='mix/:hall' element={<Hall/>}></Route>
          <Route path='mix/:hall/:mixId' element={<Mixtape/>}></Route>
          <Route path='produce' element={<Produce/>}></Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;
