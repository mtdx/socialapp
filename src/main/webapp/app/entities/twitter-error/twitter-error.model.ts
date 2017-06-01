export class TwitterError {
    constructor(
        public id?: number,
        public type?: string,
        public errorCode?: number,
        public errorMessage?: string,
        public exceptionCode?: string,
        public message?: string,
        public rateLimitStatus?: string,
        public statusCode?: number,
    ) {
    }
}
