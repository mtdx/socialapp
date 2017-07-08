import { BaseEntity } from './../../shared';

const enum TwitterErrorType {
    'UPDATE',
    'LIKE',
    'RETWEET',
    'TWEET'
}

export class TwitterError implements BaseEntity {
    constructor(
        public id?: number,
        public type?: TwitterErrorType,
        public errorCode?: number,
        public account?: string,
        public errorMessage?: string,
        public message?: string,
        public rateLimitStatus?: string,
        public statusCode?: number,
        public created_at?: any,
    ) {
    }
}
