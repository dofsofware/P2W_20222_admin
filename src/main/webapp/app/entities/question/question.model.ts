export interface IQuestion {
  id?: number;
  quiz?: string;
  reponse1?: string;
  reponse2?: string;
  reponse3?: string;
  reponse4?: string;
  bonneReponse?: string;
}

export class Question implements IQuestion {
  constructor(
    public id?: number,
    public quiz?: string,
    public reponse1?: string,
    public reponse2?: string,
    public reponse3?: string,
    public reponse4?: string,
    public bonneReponse?: string
  ) {}
}

export function getQuestionIdentifier(question: IQuestion): number | undefined {
  return question.id;
}
