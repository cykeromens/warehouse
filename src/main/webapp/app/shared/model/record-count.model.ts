export interface IRecordCount {
    id?: number;
    currencyISOCode?: string;
    dealsCount?: number;
}

export class RecordCount implements IRecordCount {
    constructor(public id?: number, public currencyISOCode?: string, public dealsCount?: number) {
    }
}
