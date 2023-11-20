const plays = require("./data/plays.json");
const invoices = require("./data/invoices.json");

function formatAmount(amount) {
  return new Intl.NumberFormat("en-US", {
    style: "currency",
    currency: "USD",
    minimumFractionDigits: 2,
  }).format(amount / 100);
}

function calculateAmount(play, perf) {
  const baseAmount = play.type === "comedy" ? 30000 : 40000;
  let thisAmount = baseAmount;

  if (perf.audience > (play.type === "comedy" ? 20 : 30)) {
    thisAmount += 1000 * (perf.audience - (play.type === "comedy" ? 20 : 30));
  }

  if (play.type === "comedy") {
    thisAmount += 500 * perf.audience;
  }

  return thisAmount;
}

function calculateTotalAmount(invoice, plays) {
  return invoice.performances.reduce((total, perf) => {
    const play = plays[perf.playID];
    return total + calculateAmount(play, perf);
  }, 0);
}

function calculateVolumeCredits(invoice, plays) {
  return invoice.performances.reduce((credits, perf) => {
    const play = plays[perf.playID];
    return (
      credits +
      Math.max(perf.audience - (play.type === "comedy" ? 20 : 30), 0) +
      (play.type === "comedy" ? Math.floor(perf.audience / 5) : 0)
    );
  }, 0);
}

function generateStatementLine(play, perf, totalAmount) {
  const thisAmount = totalAmount - (play.type === "comedy" ? 30000 : 40000);
  return `  ${play.name}: ${formatAmount(thisAmount)} (${perf.audience} seats)`;
}

function statement(invoice, plays) {
  const totalAmount = calculateTotalAmount(invoice, plays);
  const volumeCredits = calculateVolumeCredits(invoice, plays);

  const statementLines = invoice.performances.map((perf) => {
    const play = plays[perf.playID];
    return generateStatementLine(play, perf, totalAmount);
  });

  return (
    `Statement for ${invoice.customer}\n` +
    statementLines.join("\n") +
    `\nAmount owed is ${formatAmount(
      totalAmount
    )}\nYou earned ${volumeCredits} credits\n`
  );
}

console.log(statement(invoices[0], plays));
