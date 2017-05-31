import { Avatar } from '../avatar';
import { Header } from '../header';
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
        public avatar?: Avatar,
        public header?: Header,
    ) {
    }
}
