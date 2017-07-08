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
        public name?: string,
        public description?: string,
        public url?: string,
        public location?: string,
        public username?: string,
        public status?: TwitterStatus,
        public avatar?: BaseEntity,
        public header?: BaseEntity,
        public proxy?: BaseEntity,
    ) {
    }
}
