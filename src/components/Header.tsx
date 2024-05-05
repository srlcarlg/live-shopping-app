import finishIcon from "../assets/header-icons/finish-icon.svg";
import DeviceSelector from "./Header/DeviceSelector";

type Props = {
  isBroadcaster?: boolean;
};

const Header = (props: Props) => {
  return (
    <header>
      <div className="container">
        <h1>{props.isBroadcaster ? "Broadcaster" : "Viewer"}</h1>
        {props.isBroadcaster ? (
          <nav>
            <ul>
              <DeviceSelector />
              <li>
                <a href="" className="btn btn-finish">
                  <img src={finishIcon} alt="" />
                  Finish Live
                </a>
              </li>
            </ul>
          </nav>
        ) : (
          <></>
        )}
      </div>
    </header>
  );
};

export default Header;
