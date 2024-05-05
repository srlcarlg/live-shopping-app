// Requests
export interface JoinChatBroadcaster {
  username: string,
  email: string,
  password: string
}

export interface JoinChatViewer {
  username: string,
  email: string
}

export interface MessageChat {
  message: string
}

// Responses
export interface SessionBroadcast {
  sessionId: string
}
export interface MessageIncoming {
  username: string,
  email: string,
  message: string,
  isBroadcaster: boolean
}