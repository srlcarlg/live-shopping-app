import broadcastIcon from "../../assets/broadcast-icon.svg";
import editIcon from "../../assets/edit-icon.svg";
import viewIcon from "../../assets/view-icon.svg";

type Props = {};

const LiveInfo = (props: Props) => {
  return (
    <tr>
      <td>ac77bf30-b18d-439d-ba44-87d7cbcb6aba</td>
      <td>The Great Live</td>
      <td>/87d7cbcb6aba</td>
      <td>AVAILABLE</td>
      <td>10 Apr 2024</td>
      <td>
        <div className="actions">
          <a href="">
            <img src={broadcastIcon} alt="" />
            Broadcast
          </a>
          <a href="">
            <img src={viewIcon} alt="" />
            View
          </a>
          <a href="">
            <img src={editIcon} alt="" />
            Edit
          </a>
        </div>
      </td>
    </tr>
  );
};

export default LiveInfo;
