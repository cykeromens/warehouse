import {Moment} from 'moment';

export interface IInvalidDeal {
    id?: string;
    tagId?: string;
    fromIsoCode?: string;
    toIsoCode?: string;
    time?: Moment;
    amount?: number;
    source?: string;
    fileType?: string;
    uploadedOn?: Moment;
}

export class InvalidDeal implements IInvalidDeal {
    constructor(
        public id?: string,
        public tagId?: string,
        public fromIsoCode?: string,
        public toIsoCode?: string,
        public time?: Moment,
        public amount?: number,
        public source?: string,
        public fileType?: string,
        public uploadedOn?: Moment
    ) {
    }
}
