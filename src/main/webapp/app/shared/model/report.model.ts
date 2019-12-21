import {Moment} from 'moment';

export interface IReport {
    id?: string;
    fromIsoCode?: string;
    toIsoCode?: string;
    total?: number;
    lastUpdated?: Moment;
}

export class Report implements IReport {
    constructor(
        public id?: string,
        public fromIsoCode?: string,
        public toIsoCode?: string,
        public total?: number,
        public lastUpdated?: Moment
    ) {
    }
}
