import React from "react";
import { ALL_LIVES_GET } from "../../api/api";
import addIcon from "../../assets/add-icon.svg";
import LiveModal from "../Modals/LiveModal";
import LiveInfo from "./LiveInfo";

type Props = {};

const Lives = (props: Props) => {
  const [modalIsOpen, setModalIsOpen] = React.useState(false);
  const handleModalCancelClick = (clicked: boolean) => {
    setModalIsOpen(clicked);
  };

  // Get all Lives
  const sortLiveByCreatedAt = (liveArray: Live[]): Live[] => {
    return liveArray.sort((a, b) => {
      const dateA = new Date(a.createdAt);
      const dateB = new Date(b.createdAt);
      return dateB.getTime() - dateA.getTime();
    });
  };
  const [liveList, setLiveList] = React.useState([] as Live[]);
  React.useEffect(() => {
    const fetchLiveList = async () => {
      try {
        const { url, options } = ALL_LIVES_GET();
        const response = await fetch(url, options);
        const data: Live[] = await response.json();

        const sortedData = sortLiveByCreatedAt(data);
        setLiveList(sortedData);
      } catch (err) {}
    };
    fetchLiveList();
  }, [modalIsOpen]);

  return (
    <>
      {modalIsOpen ? (
        <LiveModal isEdit={false} cancelOnClick={handleModalCancelClick} />
      ) : (
        <></>
      )}
      <div className="all-lives">
        <div className="live-header">
          <h2>All Lives</h2>
          <button className="btn btn-add" onClick={() => setModalIsOpen(true)}>
            <div className="text">
              <img src={addIcon} alt="" />
              Add
            </div>
          </button>
        </div>
        <table className="table-grid">
          <thead>
            <tr className="table-header">
              <td>Title</td>
              <td>Slug</td>
              <td>Status</td>
              <td>Date</td>
              <td>Actions</td>
            </tr>
          </thead>
          <tbody>
            {liveList.map((live, index) => (
              <tr key={index}>
                <LiveInfo live={live} />
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </>
  );
};

export default Lives;
