export interface IFileLoader {
    id?: number;
    file?: any;
}

export class FileLoader implements IFileLoader {
    constructor(public id?: number, public file?: any) {}
}
