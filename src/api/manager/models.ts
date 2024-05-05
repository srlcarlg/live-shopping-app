interface Live {
  slug: string,
  title: string,
  description: string,
  password: string,
  status: string,
  createdAt: string
}

interface setBroadcaster {
  peer_id: string,
  live_password: string | null
}

interface peerIdBroadcaster {
  broadcaster_id: string
}

interface FinishLive {
  live_password: string
}