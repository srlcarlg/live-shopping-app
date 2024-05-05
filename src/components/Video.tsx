import Peer from "peerjs";
import React from "react";
import { useSession } from "../context/SessionContext";

type Props = {
  isBroadcaster?: boolean;
  wsManager?: WebSocket;
};

const Video = (props: Props) => {
  const { isBroadcaster, wsManager } = props;
  const { livePassword, cameraId, microphoneId } = useSession();

  const peerRef = React.useRef<Peer | null>(null);
  const localVideoRef = React.useRef<HTMLVideoElement>(null);
  const localStreamRef = React.useRef<MediaStream | null>(null);

  const [broadcasterPeerId, setBroadcasterPeerId] = React.useState("");

  // Broadcaster
  React.useEffect(() => {
    if ((isBroadcaster && !livePassword) || !isBroadcaster) {
      return;
    }
    // Initialize PeerJS
    peerRef.current = new Peer();

    // Sends ID to Manager WS Server
    peerRef.current.on("open", (peer_id) => {
      const data: setBroadcaster = {
        peer_id: peer_id,
        live_password: livePassword,
      };
      console.log(JSON.stringify(data));
      wsManager?.send(JSON.stringify(data));
    });

    const getMedia = async () => {
      try {
        const stream = await navigator.mediaDevices.getUserMedia({
          video: cameraId ? { deviceId: { exact: cameraId } } : true,
          audio: microphoneId ? { deviceId: { exact: microphoneId } } : true,
        });
        localStreamRef.current = stream;
        if (localVideoRef.current) {
          localVideoRef.current.srcObject = stream;
        }
      } catch (err) {
        console.error("Failed to get local stream", err);
      }
    };

    getMedia();

    // Answer the call with the local media stream
    peerRef.current.on("call", (call) => {
      if (localStreamRef.current) {
        call.answer(localStreamRef.current);
      }
    });

    return () => {
      peerRef.current?.destroy();
      localStreamRef.current?.getTracks().forEach((track) => track.stop());
    };
  }, [cameraId, microphoneId]);

  // Viewer
  const createEmptyVideoTrack = () => {
    const canvas = Object.assign(document.createElement("canvas"));
    canvas.getContext("2d").fillRect(0, 0, 0, 0);

    const stream = canvas.captureStream();
    const track = stream.getVideoTracks()[0];

    return Object.assign(track, { enabled: false });
  };

  React.useEffect(() => {
    if (isBroadcaster) {
      return;
    }

    wsManager?.addEventListener("message", (event) => {
      console.log(event.data);
      try {
        const peerId: peerIdBroadcaster = JSON.parse(event.data);
        if (peerId.broadcaster_id) {
          setBroadcasterPeerId(peerId.broadcaster_id);
        }
      } catch (error) {}
    });

    peerRef.current = new Peer();

    peerRef.current.on("open", () => {
      if (!peerRef.current || !broadcasterPeerId) return;

      const conn = peerRef.current.connect(broadcasterPeerId);
      conn.on("open", () => {
        console.log("Connection opened");
        if (peerRef.current) {
          const videoTrack = createEmptyVideoTrack();
          const mediaStream = new MediaStream([videoTrack]);

          console.log("MEDIA STREAM");
          console.log(broadcasterPeerId);
          const call = peerRef.current.call(broadcasterPeerId, mediaStream);
          call.on("stream", (remoteStream) => {
            console.log("Streaming remote video");
            if (localVideoRef.current) {
              localVideoRef.current.srcObject = remoteStream;
            }
          });
        }
      });
    });

    peerRef.current.on("call", (call) => {
      call.answer();
      call.on("stream", (remoteStream) => {
        if (localVideoRef.current) {
          localVideoRef.current.srcObject = remoteStream;
        }
      });
    });

    return () => {
      peerRef.current?.destroy();
    };
  }, [broadcasterPeerId, wsManager, peerRef, localVideoRef]);

  return (
    <video
      className="video"
      ref={localVideoRef}
      autoPlay
      playsInline
      muted
    ></video>
  );
};

export default Video;
