import { BaseEntity } from './../../shared';

export const enum TwitterStatus {
    'IDLE',
    'PENDING_UPDATE',
    'WORKING'
}

export class TwitterAccount implements BaseEntity {
    constructor(
        public id?: number,
        public email?: string,
        public consumerKey?: string,
        public consumerSecret?: string,
        public accessToken?: string,
        public accessTokenSecret?: string,
        public username?: string,
        public status?: TwitterStatus,
        public prevStatus?: TwitterStatus,
        public avatar?: BaseEntity,
        public header?: BaseEntity,
        public proxy?: BaseEntity,
        public message?: BaseEntity,
    ) {
    }
}
