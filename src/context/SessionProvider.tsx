import React from "react";
import { SessionContext, SessionContextType } from "./SessionContext";

export const SessionProvider = (props: any) => {
  const [sessionId, setSessionIdState] = React.useState<string | null>(() => {
    return sessionStorage.getItem("sessionId");
  });
  const setSessionId: SessionContextType["setSessionId"] = (
    newSessionId: any
  ) => {
    const sessionIdToUpdate =
      typeof newSessionId === "function"
        ? newSessionId(sessionId)
        : newSessionId;
    sessionStorage.setItem("sessionId", sessionIdToUpdate || "");
    setSessionIdState(sessionIdToUpdate);
  };

  const [livePassword, setLivePasswordState] = React.useState<string | null>(
    () => {
      return sessionStorage.getItem("livePassword");
    }
  );
  const setLivePassword: SessionContextType["setLivePassword"] = (
    newPassword: any
  ) => {
    sessionStorage.setItem("livePassword", newPassword || "");
    setLivePasswordState(newPassword);
  };

  const [cameraId, setCameraIdState] = React.useState<string | null>(() => {
    return sessionStorage.getItem("cameraId");
  });
  const setCameraId: SessionContextType["setCameraId"] = (newCameraId: any) => {
    sessionStorage.setItem("cameraId", newCameraId || "");
    setCameraIdState(newCameraId);
  };

  const [microphoneId, setMicrophoneIdState] = React.useState<string | null>(() => {
    return sessionStorage.getItem("microphoneId");
  });
  const setMicrophoneId: SessionContextType["setMicrophoneId"] = (newMicrophoneId: any) => {
    sessionStorage.setItem("microphoneId", newMicrophoneId || "");
    setMicrophoneIdState(newMicrophoneId);
  };

  const contextValue: SessionContextType = {
    sessionId,
    setSessionId,

    livePassword,
    setLivePassword,

    cameraId,
    setCameraId,

    microphoneId,
    setMicrophoneId,
  };

  return (
    <SessionContext.Provider value={contextValue}>
      {props.children}
    </SessionContext.Provider>
  );
};
