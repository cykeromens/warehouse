export interface IInvalidDeal {
    id?: string;
    fromIsoCode?: string;
    toIsoCode?: string;
    time?: string;
    amount?: string;
    source?: string;
    extension?: string;
    uploadedOn?: string;
    reason?: string;
}

export class InvalidDeal implements IInvalidDeal {
    constructor(
        public id?: string,
        public fromIsoCode?: string,
        public toIsoCode?: string,
        public time?: string,
        public amount?: string,
        public source?: string,
        public extension?: string,
        public uploadedOn?: string,
        public reason?: string,

    ) {
    }
}
