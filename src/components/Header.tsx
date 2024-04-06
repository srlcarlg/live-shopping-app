import finishIcon from "../assets/header-icons/finish-icon.svg";
import DeviceSelector from "./DeviceSelector";

type Props = {};

const Header = (props: Props) => {
  return (
    <header>
      <div className="container">
        <h1>Broadcaster</h1>
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
      </div>
    </header>
  );
};

export default Header;
