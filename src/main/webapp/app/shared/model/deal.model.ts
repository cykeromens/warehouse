import {Moment} from 'moment';

export interface IDeal {
    id?: string;
    fromIsoCode?: string;
    toIsoCode?: string;
    time?: Moment;
    amount?: number;
    source?: string;
    extension?: string;
    uploadedOn?: Moment;
}

export class Deal implements IDeal {
    constructor(
        public id?: string,
        public fromIsoCode?: string,
        public toIsoCode?: string,
        public time?: Moment,
        public amount?: number,
        public source?: string,
        public extension?: string,
        public uploadedOn?: Moment
    ) {}
}
