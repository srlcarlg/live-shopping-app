import React from "react";

export interface SessionContextType {
  sessionId: string | null;
  setSessionId: React.Dispatch<React.SetStateAction<string | null>>;

  livePassword: string | null;
  setLivePassword: React.Dispatch<React.SetStateAction<string | null>>;

  cameraId: string | null;
  setCameraId: React.Dispatch<React.SetStateAction<string | null>>;
  
  microphoneId: string | null;
  setMicrophoneId: React.Dispatch<React.SetStateAction<string | null>>;
}

export const SessionContext = React.createContext<SessionContextType | undefined>(undefined);

export const useSession = () => {
  const context = React.useContext(SessionContext);
  if (!context) {
    throw new Error('useSession must be used within a SessionProvider');
  }
  return context;
};
