import React, { useEffect, useState } from "react";

import cameraIcon from "../../assets/header-icons/camera-icon.svg";
import micIcon from "../../assets/header-icons/mic-icon.svg";
import { useSession } from "../../context/SessionContext";

interface MediaDeviceInfo {
  deviceId: string;
  label: string;
  kind: string;
}

function DeviceSelector() {
  const [devices, setDevices] = useState<{
    cameras: MediaDeviceInfo[];
    microphones: MediaDeviceInfo[];
  }>({ cameras: [], microphones: [] });

  const { cameraId, setCameraId, microphoneId, setMicrophoneId } = useSession();

  useEffect(() => {
    const fetchDevices = async () => {
      try {
        const devices = await navigator.mediaDevices.enumerateDevices();
        const cameras = devices.filter(
          (device) => device.kind === "videoinput"
        );
        const microphones = devices.filter(
          (device) => device.kind === "audioinput"
        );
        setDevices({ cameras, microphones });
      } catch (error) {
        console.error("Error fetching devices:", error);
      }
    };

    const requestDevicesPermission = async () => {
      navigator.mediaDevices
        .getUserMedia({ audio: true, video: true })
        .catch((error) =>
          console.error("Error requesting devices permission:", error)
        )
        .finally(() => fetchDevices());
    };

    requestDevicesPermission();
  }, []);

  const handleCameraChange = (event: React.ChangeEvent<HTMLSelectElement>) => {
    setCameraId(event.target.value);
  };

  const handleMicrophoneChange = (
    event: React.ChangeEvent<HTMLSelectElement>
  ) => {
    setMicrophoneId(event.target.value);
  };

  return (
    <>
      <li className="dropdown camera">
        <img src={cameraIcon} alt="" />
        <select
          id="cameraSelect"
          value={cameraId ? cameraId : ""}
          onChange={handleCameraChange}
        >
          <option value="">Select Camera</option>
          {devices.cameras.map((camera) => (
            <option key={camera.deviceId} value={camera.deviceId}>
              {camera.label}
            </option>
          ))}
        </select>
      </li>

      <li className="dropdown mic">
        <img src={micIcon} alt="" />
        <select
          id="microphoneSelect"
          value={microphoneId ? microphoneId : ""}
          onChange={handleMicrophoneChange}
        >
          <option value="">Select Microphone</option>
          {devices.microphones.map((microphone) => (
            <option key={microphone.deviceId} value={microphone.deviceId}>
              {microphone.label}
            </option>
          ))}
        </select>
      </li>
    </>
  );
}

export default DeviceSelector;
