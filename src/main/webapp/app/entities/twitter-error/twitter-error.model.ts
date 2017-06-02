
const enum TwitterErrorType {
    'UPDATE',
    'LIKE',
    'RETWEET',
    'TWEET'

};
export class TwitterError {
    constructor(
        public id?: number,
        public type?: TwitterErrorType,
        public errorCode?: number,
        public errorMessage?: string,
        public message?: string,
        public rateLimitStatus?: string,
        public statusCode?: number,
    ) {
    }
}
