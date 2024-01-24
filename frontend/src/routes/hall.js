import { useParams } from 'react-router-dom'

const Hall = () => {
  const { hall } = useParams()
  return (
    <div className="Hall">
      <p>{hall}</p>
    </div>
  );
}

export default Hall