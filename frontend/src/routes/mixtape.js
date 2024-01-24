import { useParams } from 'react-router-dom'
import axios from "axios"
import Reel from './mixtapes/reel';
import Metadata from './mixtapes/metadata';
import Artwork from './mixtapes/artwork';

const Mixtape = () => {
  const { hall } = useParams()
  const { mixId } = useParams()

  axios.get(`http://127.0.0.1:8080/backend/mix/${hall}/${mixId}`)
  .then((response) => {
    console.log(response);
  })
  .catch((error) => {
    console.log(error);
  })
  
  let a = 'meta' // 백엔드에서 믹스테이프 종류 받아오기
  return (
    <div className="Mixtape">
      <p>Hall name. {hall}</p>
      <p>Mixtape ID. {mixId}</p>
      {
        (a === 'reel' && <Reel/>) ||
        (a === 'artwork' && <Artwork/>)||
        (a === 'metadata' && <Metadata/>)
      }
    </div>
  );
}

export default Mixtape;