import { Pessoa } from './pessoa'

export interface PagedResult<T> {

    content: T[];

    totalElements: number;

    totalPages: number;

    last: boolean;

    first : boolean;

    'number': number;

    numberOfElements: number;

    size : number;

}