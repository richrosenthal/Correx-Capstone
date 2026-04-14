export type CapaStage =
  | 'DRAFT'
  | 'TRIAGE'
  | 'INVESTIGATION'
  | 'ACTION_PLAN'
  | 'IMPLEMENTATION'
  | 'EFFECTIVENESS_CHECK'
  | 'CLOSED';

export const CAPA_STAGES: CapaStage[] = [
  'DRAFT',
  'TRIAGE',
  'INVESTIGATION',
  'ACTION_PLAN',
  'IMPLEMENTATION',
  'EFFECTIVENESS_CHECK',
  'CLOSED',
];

export const CAPA_STAGE_LABELS: Record<CapaStage, string> = {
  DRAFT: 'Draft',
  TRIAGE: 'Triage',
  INVESTIGATION: 'Investigation',
  ACTION_PLAN: 'Action Plan',
  IMPLEMENTATION: 'Implementation',
  EFFECTIVENESS_CHECK: 'Effectiveness Check',
  CLOSED: 'Closed',
};

export function getNextCapaStage(stage: CapaStage): CapaStage | null {
  const index = CAPA_STAGES.indexOf(stage);
  if (index === -1 || index === CAPA_STAGES.length - 1) {
    return null;
  }
  return CAPA_STAGES[index + 1];
}

export function isStageAtLeast(stage: CapaStage, target: CapaStage): boolean {
  return CAPA_STAGES.indexOf(stage) >= CAPA_STAGES.indexOf(target);
}
