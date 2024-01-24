import axios from "axios"
import { useState } from "react";

const Produce = () => {
  const [title, setTitle] = useState([]);
  const [contents, setContents] = useState([]);

  const create = () => {
    axios.post('http://127.0.0.1:8080/backend/mix/create', {
    hall: 'hall',
    title,
    contents,
    createdBy: 'H4n_uL',
    type: 'metadata'
  })
  .then(function (response) {
    console.log(response);
  })
  .catch(function (error) {
    console.log(error);
  })}

  return (
    <div className="Produce" style={{display:'flex', flexDirection: 'column'}}>
      <input
        id="titlearea"
        type="text"
        value={title}
        onChange={(event) => setTitle(event.target.value)}
        style={{ marginLeft: '10%', marginBottom: '30px', marginRight: '20%' }}
      />
      <textarea
        id="contentsarea"
        type="text"
        value={contents}
        onChange={(event) => setContents(event.target.value)}
        style={{ marginLeft: '10%', marginBottom: '30px', marginRight: '20%' }}
      />
      <button onClick={create}
        style={{ marginBottom: '10px', height: '50px', width: '100px', alignSelf: 'center' }}>Upload</button>
    </div>
  );
}

export default Produce;