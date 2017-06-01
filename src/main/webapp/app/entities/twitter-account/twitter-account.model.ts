
export const enum TwitterStatus {
    'IDLE',
    'PENDING_UPDATE',
    'LIKES_JOB'

};
import { Avatar } from '../avatar';
import { Header } from '../header';
import { Proxy } from '../proxy';
export class TwitterAccount {
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
        public avatar?: Avatar,
        public header?: Header,
        public proxy?: Proxy,
    ) {
    }
}
