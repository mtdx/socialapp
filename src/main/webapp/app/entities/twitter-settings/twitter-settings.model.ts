export class TwitterSettings {
    constructor(
        public id?: number,
        public maxLikes?: number,
        public hasDefaultProfileImage?: boolean,
        public hasNoDescription?: boolean,
        public accountAgeLessThan?: number,
        public minActivity?: number,
        public followingToFollowersRatio?: number,
        public likesToTweetsRatio?: number,
        public notLikeTweetsOlderThan?: number,
    ) {
        this.hasDefaultProfileImage = false;
        this.hasNoDescription = false;
    }
}
